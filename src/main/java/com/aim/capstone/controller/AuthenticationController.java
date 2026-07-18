package com.aim.capstone.controller;

import java.io.IOException;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.aim.capstone.auth.*;
import com.aim.capstone.service.AuthenticationService;
import jakarta.servlet.http.*;

import lombok.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthenticationController
{
  
  private final AuthenticationService authService;
  

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
    @RequestBody RegisterRequest request,
    HttpServletResponse response
  )
  {
    return ResponseEntity.ok(authService.register(request, response));
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
    @RequestBody AuthenticationRequest request,
    HttpServletResponse response
  )
  {
    return ResponseEntity.ok(authService.authenticate(request, response));
  }

  @GetMapping("/refresh")
  public HttpStatus refresh(
    @CookieValue(name = "jwt", defaultValue = "none") String jwtRefreshCookie,
    HttpServletResponse response
  ) throws IOException
  {
    return authService.refresh(jwtRefreshCookie, response);
  }

}
