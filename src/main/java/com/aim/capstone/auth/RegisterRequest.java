package com.aim.capstone.auth;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest
{

  private String username;
  private String password;

}
