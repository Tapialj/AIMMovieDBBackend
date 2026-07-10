package com.aim.capstone.repository;

import com.aim.capstone.model.User;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long>
{
 
  boolean existsByUsername(String username);
  
  Optional<User> findByUsername(String username);

}
