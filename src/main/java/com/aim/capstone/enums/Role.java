package com.aim.capstone.enums;

import lombok.*;


@Getter
@AllArgsConstructor
public enum Role
{
  
  GUEST(1, "GUEST"),
  USER(2, "USER"),
  ADMIN(3, "ADMIN");


  private final long id;
  private final String name;

}
