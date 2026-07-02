package com.aim.capstone.Security;

import com.aim.capstone.repository.TokenRepository;

import java.io.*;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import lombok.extern.slf4j.*;
import lombok.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
  
  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException
  {
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String username;
    
    if(authHeader == null || !authHeader.startsWith("Bearer"))
    {
      filterChain.doFilter(request, response);
      return;
    }
    
    jwt = authHeader.substring(7);
    username = jwtService.extractUsername(jwt);
    
    if(username != null && SecurityContextHolder.getContext().getAuthentication() == null)
    {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
      boolean isTokenValid = tokenRepository.findByToken(jwt).map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);
      
      if(jwtService.isTokenValid(jwt, userDetails) && isTokenValid)
      {
        UsernamePasswordAuthenticationToken authToken = 
          new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
          );

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }

    filterChain.doFilter(request, response);
  }

}
