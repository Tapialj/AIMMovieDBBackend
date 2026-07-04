package com.aim.capstone.enums;

public enum Roles
{
  
  GUEST("GUEST"),
  USER("USER"),
  ADMIN("ADMIN");


  private final String name;

  private Roles(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

}
