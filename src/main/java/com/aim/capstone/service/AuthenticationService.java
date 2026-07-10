package com.aim.capstone.service;

import com.aim.capstone.model.User;
import com.aim.capstone.auth.*;
import com.aim.capstone.enums.Roles;
import com.aim.capstone.enums.TokenType;
import com.aim.capstone.repository.*;
import com.aim.capstone.security.*;
import jakarta.servlet.http.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.*;

import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.*;
import org.springframework.web.server.ResponseStatusException;
import lombok.*;
import lombok.extern.slf4j.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService
{

  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;


  public AuthenticationResponse register(RegisterRequest request, HttpServletResponse response)
  {
    if(userRepository.existsByUsername(request.getUsername()))
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists.");

    User user = User
      .builder()
      .username(request.getUsername())
      .password(passwordEncoder.encode(request.getPassword()))
      .role(Roles.USER)
      .build();
    
    User savedUser = userRepository.save(user);
    String jwtToken = jwtService.generateToken(user);
    List<String> roles = Arrays.asList(Roles.USER.getName());
    Cookie refreshToken = jwtService.generateRefreshToken(user);
    log.debug("New User {} has been created with roles {}", savedUser.getUsername(), roles);

    response.addCookie(refreshToken);
    saveUserToken(savedUser, refreshToken.getValue(), TokenType.REFRESH);
    saveUserToken(savedUser, jwtToken, TokenType.ACCESS);

    return AuthenticationResponse
      .builder()
      .user(user)
      .token(jwtToken)
      .roles(roles)
      .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletResponse response)
  {
    Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(auth);
    User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
    String jwtToken = jwtService.generateToken(user);
    Cookie refreshToken = jwtService.generateRefreshToken(user);
    List<String> roles = user.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
    List<Token> validUserTokens = tokenRepository.findAllValidTokenByUserId(user.getId());
    log.debug("User {} has been authenticated with", user.getUsername());

    response.addCookie(refreshToken);
    revokeAllUserTokens(validUserTokens);
    saveUserToken(user, refreshToken.getValue(), TokenType.REFRESH);
    saveUserToken(user, jwtToken, TokenType.ACCESS);

    return AuthenticationResponse
      .builder()
      .user(user)
      .token(jwtToken)
      .roles(roles)
      .build();
  }

  public HttpStatus refresh(String jwtRefreshCookie, HttpServletResponse response) throws IOException
  {
    final String username;
  
    if(jwtRefreshCookie.equalsIgnoreCase("none"))
    {
      log.warn("Request for refresh didn't have a token.");
      return HttpStatus.UNAUTHORIZED;
    }
    
    username = jwtService.extractUsername(jwtRefreshCookie);

    //If no username, return forbidden
    if(username == null)
    {
      log.warn("Request token didn't have an associated user");
      return HttpStatus.FORBIDDEN;
    }
    
    User user = userRepository.findByUsername(username).orElseThrow();

    //Check if repository refresh token is expired or revoked
    boolean isTokenValid = tokenRepository.findByToken(jwtRefreshCookie).map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);
    
    //If refresh token is valid as a token and in repo
    if(jwtService.isTokenValid(jwtRefreshCookie, user) && isTokenValid)
    {
      String accessToken;
      List<Token> validUserTokens = tokenRepository.findAllValidAccessTokenByUserId(user.getId(), TokenType.ACCESS);

      //If there is a valid token, use it instead of generating a new one
      if(!validUserTokens.isEmpty())
      {
        accessToken = validUserTokens.get(0).getToken();
        validUserTokens.remove(0);
        revokeAllUserTokens(validUserTokens);
      }
      else
      {
        accessToken = jwtService.generateToken(user);
        validUserTokens = removeDuplicateIfExists(validUserTokens, accessToken);
        revokeAllUserTokens(validUserTokens);
      }
      
      if(!tokenRepository.findByToken(accessToken).isPresent())
        saveUserToken(user, accessToken, TokenType.ACCESS);

      AuthenticationResponse authResponse = AuthenticationResponse
        .builder()
        .user(user)
        .token(accessToken)
        .build();
      
      ObjectMapper mapper = JsonMapper.builder()
        .addModule(new JavaTimeModule()
          .addSerializer(
            new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
          )
          .addDeserializer(
            LocalDateTime.class,
            new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
          )
        )
        .build();
      
      mapper.writeValue(response.getOutputStream(), authResponse);

      log.info("User {} has been issued a new access token", user.getUsername());

      return HttpStatus.OK;
    }
    else
    {
      log.warn("Request token was invalid/expired/revoked for user {}", user.getUsername());
      return HttpStatus.FORBIDDEN;
    }
  }

  private void saveUserToken(User user, String jwtToken, TokenType type)
  {
    Token token = Token
      .builder()
      .userId( user.getId())
      .token(jwtToken)
      .tokenType(type)
      .expirationDate(new Date(System.currentTimeMillis() + (type.getExpirationTimeSeconds() * 1000)))
      .expired(false)
      .revoked(false)
      .build();
    
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(List<Token> validUserTokens)
  {
    if(validUserTokens.isEmpty())
      return;

    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });

    tokenRepository.saveAll(validUserTokens);
  }

  private List<Token> removeDuplicateIfExists(List<Token> tokenList, String newToken)
  {
    List<Token> newList = tokenList.stream().filter(t -> t.getToken() != newToken).collect(Collectors.toList());
    return newList;
  }

}
