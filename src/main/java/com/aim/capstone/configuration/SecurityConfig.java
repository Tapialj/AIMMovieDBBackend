package com.aim.capstone.configuration;

import com.aim.capstone.enums.Role;
import com.aim.capstone.security.JwtAuthenticationFilter;

import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
// import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig
{

  @Value("#{'${cors.url}'.split(',')}")
  List<String> corsUrls;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final AuthenticationManager authenticationManager;
  private final LogoutHandler logoutHandler;

  
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
  {
    return http
      .csrf((csrf) -> csrf.disable())
      .cors(Customizer.withDefaults())
      .authorizeHttpRequests((authorizeHttpRequests) ->
        authorizeHttpRequests
          .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
          .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
          .requestMatchers(HttpMethod.DELETE, "/api/actors/*/movies/**")
            .hasAnyAuthority(Role.USER.getName(), Role.ADMIN.getName())
          .requestMatchers(HttpMethod.POST, "/api/**").hasAnyAuthority(Role.USER.getName(), Role.ADMIN.getName())
          .requestMatchers(HttpMethod.PUT, "/api/**").hasAnyAuthority(Role.USER.getName(), Role.ADMIN.getName())
          .requestMatchers(HttpMethod.DELETE, "/api/**").hasAuthority(Role.ADMIN.getName())
          // .anyRequest().authenticated()
      )
      .sessionManagement((sessionManagement) ->
        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      .authenticationManager(authenticationManager)
      .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
      // .exceptionHandling((exception) -> 
      //   exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
      // )
      .logout((logoutCustomizer) ->
        logoutCustomizer
          .logoutUrl("/api/logout")
          .addLogoutHandler(logoutHandler)
          .logoutSuccessHandler((req, res, auth) ->
            SecurityContextHolder.clearContext()
          )
      )
      .build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    
    configuration.setAllowedOrigins(corsUrls);
    configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedHeaders(List.of("Authorization", "Content-type"));
    source.registerCorsConfiguration("/**", configuration);
    
    return source;
  }

}

