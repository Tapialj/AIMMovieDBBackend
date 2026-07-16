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

  public List<Actor> getMovieActors(Long id)
  {
    return movieRepository.findMovieActors(id);
  }

  public Movie getRandomMovie()
  {
    List<Long> movieIds = movieRepository.getMovieIds();
    long randomIndex = new Random().nextLong(movieIds.size());

    return movieRepository.findById(movieIds.get((int) randomIndex)).get();
  }

  public List<Movie> getRandomMovies()
  {
    List<Long> movieIds = movieRepository.getMovieIds();
    List<Movie> randos = new ArrayList<Movie>();

    do
    {
      long randomIndex = new Random().nextLong(movieIds.size());
      if(!existsInList(randos, movieIds.get((int) randomIndex)))
      {
        Long tempId = movieIds.get((int) randomIndex);
        Optional<Movie> tempMovie = movieRepository.findById(tempId);
        randos.add(tempMovie.get());
      }
    }
    while(randos.size() < 4);

    return randos;
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

  private boolean existsInList(List<Movie> list, long id)
  {
    return list.stream().anyMatch(a -> id == a.getId());
  }

}
