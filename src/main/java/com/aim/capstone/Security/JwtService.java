package com.aim.capstone.security;

import java.util.*;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class JwtService
{

  private final String key = System.getenv("SECRET_KEY");
  private final long jwtExp = Long.parseLong(System.getenv("JWT_EXP_SEC"));
  private final long jwtRefreshExp = Long.parseLong(System.getenv("JWT_REFRESH_SEC"));


  public String extractUsername(String token)
  {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
  {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String generateToken(UserDetails userDetails)
  {
    return generateToken(new HashMap<>(), userDetails);
  }

  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails)
  {
    return buildToken(extraClaims, userDetails, jwtExp);
  }

  public Cookie generateRefreshToken(UserDetails userDetails)
  {
    return generateRefreshCookie(buildToken(new HashMap<>(), userDetails, jwtRefreshExp));
  }

  private Cookie generateRefreshCookie(String token)
  {
    Cookie cookie = new Cookie("jwt", token);
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    cookie.setAttribute("SameSite", "none");
    
    cookie.setMaxAge((int)jwtRefreshExp);

    return cookie;
  }

  private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration)
  {
    return Jwts
      .builder()
      .claims(extraClaims)
      .subject(userDetails.getUsername())
      .issuedAt(new Date(System.currentTimeMillis()))
      .expiration(new Date(System.currentTimeMillis() + (expiration * 1000)))
      .signWith(getSigningKey(), SIG.HS256)
      .compact();
  }

  public boolean isTokenValid(String token, UserDetails userDetails)
  {
    final String username = extractUsername(token);
    return username.equals(userDetails.getUsername());
  }

  private Claims extractAllClaims(String token)
  {
    return Jwts
      .parser()
      .verifyWith(getSigningKey())
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }

  private SecretKey getSigningKey()
  {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
  }

}
