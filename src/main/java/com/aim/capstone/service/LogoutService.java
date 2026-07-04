package com.aim.capstone.service;

import com.aim.capstone.Security.Token;
import com.aim.capstone.repository.TokenRepository;

import java.util.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.*;

import lombok.*;
import lombok.extern.slf4j.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler
{

  private final TokenRepository tokenRepository;


  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
  {
    final String authHeader = request.getHeader("Authorization");
    final List<Cookie> cookies = request.getCookies() != null 
      ? Arrays.asList(request.getCookies())
      : new ArrayList<Cookie>();
    final Cookie refreshToken = cookies.stream().filter(c -> "jwt".equals(c.getName())).findAny().orElse(null);
    final String accessToken;

    if(authHeader == null || !authHeader.startsWith("Bearer "))
      return;
    else if(refreshToken == null)
      return;

    accessToken = authHeader.substring(7);
    refreshToken.setMaxAge(0);
    response.addCookie(refreshToken);
    
    Token storedToken = tokenRepository.findByToken(accessToken).orElse(null);
    Token storedRefreshToken = tokenRepository.findByToken(refreshToken.getValue()).orElse(null);

    if(storedToken != null)
    {
      storedToken.setExpired(true);
      storedToken.setRevoked(true);
      tokenRepository.save(storedToken);
    }
    
    if(storedRefreshToken != null)
    {
      storedRefreshToken.setExpired(true);
      storedRefreshToken.setRevoked(true);
      tokenRepository.save(storedRefreshToken);
    }
    
    SecurityContextHolder.clearContext();
  }
  
}

