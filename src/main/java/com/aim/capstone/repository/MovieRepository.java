package com.aim.capstone.repository;

import java.time.*;
import java.util.*;

import com.aim.capstone.model.Movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface MovieRepository extends JpaRepository<Movie, Long>
{
  
  Optional<Movie> findByTitle(String title);

  Optional<Movie> findByReleaseDate(LocalDate releaseDate);

  @Query(
    """
    SELECT MAX(m.id)
    FROM Movie m
    """
  )
  long getMax();

}
