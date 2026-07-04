package com.aim.capstone.repository;

import com.aim.capstone.model.Genre;

import org.springframework.data.jpa.repository.JpaRepository;


public interface GenreRepository extends JpaRepository <Genre, Long>
{
  
}
