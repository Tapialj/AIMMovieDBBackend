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
    return getRandom();
  }

  public List<Movie> getRandomMovies()
  {
    List<Movie> randos = new ArrayList<Movie>();

    do
    {
      Movie testMovie = getRandom();

      if(!existsInList(randos, testMovie.getId()))
        randos.add(testMovie);
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

  public Movie updateMovieActorRemove(long id)
  {
    Optional<Movie> existing = movieRepository.findById(id);

    if(existing.isPresent())
    {
      return movieRepository.save(existing.get());
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

  private Movie getRandom()
  {
    long max = movieRepository.getMax();
    long randId;
    Optional<Movie> temp = Optional.empty();

    do
    {
      randId = new Random().nextLong(max);
      temp = movieRepository.findById(randId);
    }
    while(temp.isEmpty());

    return temp.get();
  }

}
