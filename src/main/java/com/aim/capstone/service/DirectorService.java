package com.aim.capstone.service;

import java.util.*;

import com.aim.capstone.model.Director;
import com.aim.capstone.model.Movie;
import com.aim.capstone.repository.DirectorRepository;
import com.aim.capstone.repository.MovieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.*;


@Service
@RequiredArgsConstructor
public class DirectorService 
{
  
  private final DirectorRepository directorRepository;
  private final MovieRepository movieRepository;


  public List<Director> getDirectors()
  {
    return directorRepository.findAll();
  }

  public Director getDirector(Long id) 
  {
    return directorRepository.findById(id).orElseThrow(() -> new  ResponseStatusException(HttpStatus.NOT_FOUND,"Director Does not exist."));
  }

  public List<Movie> getDirectorMovies(Long id)
  {
    return directorRepository.findMoviesByDirectorId(id);
  }

  public Director getRandomDirector()
  {
    List<Long> directorIds = directorRepository.getDirectorIds();
    long randomIndex = new Random().nextLong(directorIds.size());

    return directorRepository.findById(directorIds.get((int) randomIndex)).get();
  }

  public List<Director> getRandomDirectors()
  {
    List<Long> directorIds = directorRepository.getDirectorIds();
    List<Director> randos = new ArrayList<Director>();

    do
    {
      long randomIndex = new Random().nextLong(directorIds.size());
      if(!existsInList(randos, directorIds.get((int) randomIndex)))
        randos.add(directorRepository.findById(directorIds.get((int) randomIndex)).get());
    }
    while(randos.size() < 4);

    return randos;
  }

  public Director addNewDirector(Director director)
  {
    Optional<Director> directorOptional = directorRepository.findDirectorByName(director.getLastName(), director.getFirstName());

    if(directorOptional.isPresent())
    {
      throw new  ResponseStatusException(HttpStatus.CONFLICT,"Director already exists.");
    }

    return directorRepository.save(director);
  }

  public void deleteDirector(Long directorId)
  {
    directorRepository.deleteById(directorId);
  }

  public Director updateDirector(Director director)
  {
    Optional<Director> existing = directorRepository.findById(director.getId());

    if(existing.isPresent())
    {
      return directorRepository.save(director);
    }
    else
    {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Director not found");
    }
  }

  @Transactional
  public Movie updateMovieAddDirector(Director director, Long movieId)
  {
    Optional<Movie> movieOptional = movieRepository.findById(movieId);
    
    if(movieOptional.isPresent())
    {
      Movie updateMovie = movieOptional.get();

      updateMovie.addDirector(director);

      return movieRepository.save(updateMovie);
    }
    else
    {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found");  
    }
  }
  
  @Transactional
  public void updateMovieRemoveDirector(Long directorId, Long movieId)
  {
    Optional<Director> directorOptional = directorRepository.findById(directorId);
    Optional<Movie> movieOptional = movieRepository.findById(movieId);
    
    if(directorOptional.isPresent() && movieOptional.isPresent())
    {
      Director updateDirector = directorOptional.get();
      Movie updateMovie = movieOptional.get();

      updateMovie.removeDirector(updateDirector);
      
      movieRepository.save(updateMovie);
    }
    else
    {
      if(directorOptional.isEmpty())
      {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor not found");  
      }
      else
      {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found");
      }
    }
  }

  private boolean existsInList(List<Director> list, long id)
  {
    return list.stream().anyMatch(a -> id == a.getId());
  }

}
