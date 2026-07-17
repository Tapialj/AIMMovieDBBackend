package com.aim.capstone.controller;

import java.util.*;

import com.aim.capstone.model.*;
import com.aim.capstone.service.MovieService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/movies")
public class MovieController 
{

  private final MovieService movieService;


  @GetMapping
  public List<Movie> getMovies()
  {
    return movieService.getMovies();
  }

  @GetMapping(path = "{movieId}")
  public Movie getMovie(@PathVariable Long movieId)
  {
    return movieService.getMovie(movieId);
  }

  @GetMapping(path = "{movieId}/actors")
  public List<Actor> getMovieActors(@PathVariable Long movieId)
  {
    return movieService.getMovieActors(movieId);
  }

  @GetMapping(path = "{movieId}/directors")
  public List<Director> getMovieDirectors(@PathVariable Long movieId)
  {
    return movieService.getMovieDirectors(movieId);
  }

  @GetMapping(path = "random")
  public Movie getRandomMovie()
  {
    return movieService.getRandomMovie();
  }

  @GetMapping(path = "random-list")
  public List<Movie> getRandomMovies()
  {
    return movieService.getRandomMovies();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Movie createMovie(@RequestBody Movie movie)
  {
    return movieService.addNewMovie(movie);
  }

  @DeleteMapping(path = "{movieId}")
  public void deleteMovie(@PathVariable Long movieId)
  {
    movieService.deleteMovie(movieId);
  }

  @PutMapping(path = "{movieId}")
  public Movie updateMovie(@RequestBody Movie movie)
  {
    return movieService.updateMovie(movie);
  }

}
