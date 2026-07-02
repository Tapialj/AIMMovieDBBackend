package com.aim.capstone.service;

import java.util.*;

import com.aim.capstone.model.*;
import com.aim.capstone.repository.MovieRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.*;


@Service
@RequiredArgsConstructor
public class MovieService 
{
 
  private final MovieRepository movieRepository;


  public List<Movie> getMovies()
  {
    return movieRepository.findAll();
  }

  public Movie getMovie(Long id)
  {
    return movieRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie does not exist."));
  }

  public Movie addNewMovie(Movie movie)
  {
    Optional<Movie> movieTitleOptional = movieRepository.findByTitle(movie.getTitle());
    Optional<Movie> movieReleaseOptional = movieRepository.findByReleaseDate(movie.getReleaseDate());

    if(movieTitleOptional.isPresent() && movieReleaseOptional.isPresent())
    {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Movie already exists.");
    }

    return movieRepository.save(movie);
  }

  public void deleteMovie(Long id)
  {
    movieRepository.deleteById(id);
  }

  public Movie updateMovie(Movie movie)
  {
    Optional<Movie> existing = movieRepository.findById(movie.getId());

    if(existing.isPresent())
    {
      return movieRepository.save(movie);
    }
    else
    {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found");
    }
  }

}
