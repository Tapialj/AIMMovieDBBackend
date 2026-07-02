package com.aim.capstone.auth;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest
{
  
  private String username;
  private String password;

}
