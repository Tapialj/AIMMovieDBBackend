package com.aim.capstone.model;

import com.aim.capstone.Security.Token;
import com.aim.capstone.enums.Roles;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

import jakarta.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails
{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;
  private String username;
  @Getter(value=AccessLevel.NONE)
  @JsonIgnore
  private String password;
  @Enumerated(EnumType.STRING)
  private Roles role;
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Token> tokens;


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities()
  {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getPassword()
  {
    return null;
  }
  
}
