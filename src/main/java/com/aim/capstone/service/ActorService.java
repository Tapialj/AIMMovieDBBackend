package com.aim.capstone.service;

import java.util.*;

import com.aim.capstone.model.Actor;
import com.aim.capstone.model.Movie;
import com.aim.capstone.repository.ActorRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.*;


@Service
@RequiredArgsConstructor
public class ActorService
{
  
  private final ActorRepository actorRepository;


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
    return actorRepository.findMovieByMovieCast(id);
  }

  public Actor getRandomActor()
  {
    return getRandom();
  }

  public List<Actor> getRandomActors()
  {
    List<Actor> randos = new ArrayList<Actor>();

    do
    {
      Actor testActor = getRandom();

      if(!randos.contains(testActor))
        randos.add(testActor);
    }
    while(randos.size() >= 4);

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

  private Actor getRandom()
  {
    long max = actorRepository.getMax();
    long randId;
    Optional<Actor> temp = Optional.empty();

    do
    {
      randId = new Random().nextLong(max);
      temp = actorRepository.findById(randId);
    }
    while(temp.isEmpty());

    return temp.get();
  }

}
