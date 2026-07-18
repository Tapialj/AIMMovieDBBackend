package com.aim.capstone.model;

import com.aim.capstone.enums.Role;
import com.aim.capstone.security.Token;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;
import java.util.stream.Collectors;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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
  @ElementCollection(targetClass = Role.class)
  @CollectionTable(
    name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id")
  )
  @Column(name = "role_id")
  @Fetch(FetchMode.JOIN)
  private List<Role> roles;
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Token> tokens;


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities()
  {
    return roles
      .stream()
      .map((role) -> new SimpleGrantedAuthority(role.name()))
      .collect(Collectors.toList());
  }

  @Override
  public String getPassword()
  {
    return password;
  }
  
}
