package com.aim.capstone.security;

import com.aim.capstone.enums.TokenType;
import com.aim.capstone.model.User;
import com.fasterxml.jackson.annotation.*;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
public class Token implements Comparable<Token>
{
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "token_id")
  private Long id;
  @Column(unique = true)
  private String token;
  @Enumerated(EnumType.STRING)
  @Column(name = "token_type")
  private TokenType tokenType;
  @Column(name = "expiration_date")
  private Date expirationDate;
  private boolean expired;
  private boolean revoked;
  @Column(name = "user_id")
  private Long userId;
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
  @JsonIgnore
  private User user;


  @Override
  public int compareTo(Token o)
  {
    return this.getId().compareTo(o.getId());
  }

}

