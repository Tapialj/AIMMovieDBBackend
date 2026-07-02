package com.aim.capstone.repository;

import com.aim.capstone.Security.Token;
import com.aim.capstone.enums.TokenType;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface TokenRepository extends JpaRepository<Token, Long>
{
  
  @Query(value = 
    """  
    SELECT t 
    FROM Token t 
    INNER JOIN User u
    ON t.user.id = u.id
    WHERE u.id = ?1
    AND (
      t.expired = false
      OR t.revoked = false
    )
    """
  )
  List<Token> findAllValidTokenByUserId(Long id);

  @Query(value = 
    """  
    SELECT t FROM Token t INNER JOIN User u
    ON t.user.id = u.id
    WHERE u.id = ?1 
    AND t.tokenType = ?2
    AND (
      t.expired = false 
      OR t.revoked = false
    )
    """
  )
  List<Token> findAllValidAccessTokenByUserId(Long id, TokenType type);

  List<Token> findByUserId(Long id);

  Optional<Token> findByToken(String token);

}

