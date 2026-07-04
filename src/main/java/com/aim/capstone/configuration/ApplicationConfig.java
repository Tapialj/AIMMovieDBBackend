package com.aim.capstone.configuration;

import com.aim.capstone.repository.UserRepository;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import lombok.*;


@Configuration
@RequiredArgsConstructor
public class ApplicationConfig
{

  private final UserRepository repository;


  @Bean
  public UserDetailsService userDetailsService()
  {
    return new UserDetailsService()
    {
      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
      }
    };
  }

  @Bean
  public AuthenticationManager authenticationManager()
  {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService());

    authenticationProvider.setPasswordEncoder(passwordEncoder());

    return new ProviderManager(authenticationProvider);
  }


  @Bean
  public PasswordEncoder passwordEncoder()
  {
    return new BCryptPasswordEncoder();
  }

}
