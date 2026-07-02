package com.aim.capstone.auth;

import java.util.*;

import com.aim.capstone.model.User;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse
{

  private User user;
  private String token;
  private List<String> roles;

}
