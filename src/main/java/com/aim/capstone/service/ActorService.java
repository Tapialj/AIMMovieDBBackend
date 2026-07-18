package com.aim.capstone.service;

import java.util.*;

import com.aim.capstone.model.Actor;
import com.aim.capstone.model.Movie;
import com.aim.capstone.repository.ActorRepository;
import com.aim.capstone.repository.MovieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.*;


@Service
@RequiredArgsConstructor
public class ActorService
{
  
  private final ActorRepository actorRepository;
  private final MovieRepository movieRepository;


  public List<Actor> getActors()
  {
    return actorRepository.findAll();
  }

  public Actor getActor(Long id) 
  {
    return actorRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor Does not exist."));
  }

  public List<Movie> getActorMovies(Long id)
  {
    return actorRepository.findMoviesByActorId(id);
  }

  public Actor getRandomActor()
  {
    List<Long> actorIds = actorRepository.getActorIds();
    long randomIndex = new Random().nextLong(actorIds.size());

    return actorRepository.findById(actorIds.get((int) randomIndex)).get();
  }

  public List<Actor> getRandomActors()
  {
    List<Long> actorIds = actorRepository.getActorIds();
    List<Actor> randos = new ArrayList<Actor>();

    do
    {
      long randomIndex = new Random().nextLong(actorIds.size());
      if(!existsInList(randos, actorIds.get((int) randomIndex)))
        randos.add(actorRepository.findById(actorIds.get((int) randomIndex)).get());
    }
    while(randos.size() < 4);

    return randos;
  }

  public Actor addNewActor(Actor actor)
  {
    Optional<Actor> actorOptional = actorRepository.findActorByName(actor.getLastName(), actor.getFirstName());

    if(actorOptional.isPresent())
    {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Actor already exists.");
    }

    return actorRepository.save(actor);
  }

  public void deleteActor(Long actorId)
  {
    actorRepository.deleteById(actorId);
  }

  public Actor updateActor(Actor actor)
  {
    Optional<Actor> existing = actorRepository.findById(actor.getId());

    if(existing.isPresent())
    {
      return actorRepository.save(actor);
    }
    else
    {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor not found");
    }
  }

  @Transactional
  public Movie updateMovieAddActor(Actor actor, Long movieId)
  {
    Optional<Movie> movieOptional = movieRepository.findById(movieId);
    
    if(movieOptional.isPresent())
    {
      Movie updateMovie = movieOptional.get();

      updateMovie.addActor(actor);

      return movieRepository.save(updateMovie);
    }
    else
    {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found");  
    }
  }
  
  @Transactional
  public void updateMovieRemoveActor(Long actorId, Long movieId)
  {
    Optional<Actor> actorOptional = actorRepository.findById(actorId);
    Optional<Movie> movieOptional = movieRepository.findById(movieId);
    
    if(actorOptional.isPresent() && movieOptional.isPresent())
    {
      Actor updateActor = actorOptional.get();
      Movie updateMovie = movieOptional.get();

      updateMovie.removeActor(updateActor);
      
      movieRepository.save(updateMovie);
    }
    else
    {
      if(actorOptional.isEmpty())
      {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor not found");  
      }
      else
      {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found");
      }
    }
  }

  private boolean existsInList(List<Actor> list, long id)
  {
    return list.stream().anyMatch(a -> id == a.getId());
  }

}
