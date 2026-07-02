package com.aim.capstone.enums;


public enum TokenType
{
  
  
  ACCESS(Long.parseLong(System.getenv("JWT_EXP_SEC"))),
  REFRESH(Long.parseLong(System.getenv("JWT_REFRESH_SEC")));
  
  
  private final Long expirationTimeSeconds;

  private TokenType(Long expirationTimeSeconds)
  {
    this.expirationTimeSeconds = expirationTimeSeconds;
  }

  public Long getExpirationTimeSeconds()
  {
    return expirationTimeSeconds;
  }

}
