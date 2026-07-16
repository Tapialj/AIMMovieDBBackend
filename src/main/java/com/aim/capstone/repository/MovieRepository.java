package com.aim.capstone.repository;

import java.time.*;
import java.util.*;

import com.aim.capstone.model.Actor;
import com.aim.capstone.model.Movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface MovieRepository extends JpaRepository<Movie, Long>
{
  
  Optional<Movie> findByTitle(String title);

  Optional<Movie> findByReleaseDate(LocalDate releaseDate);

  @Query(
    """
    SELECT a
    FROM Actor a
    JOIN a.movies m
    WHERE m.id = ?1
    """
  )
  List<Actor> findMovieActors(long id);

  @Query(
    """
    SELECT m.id
    FROM Movie m
    """
  )
  List<Long> getMovieIds();
  
}
