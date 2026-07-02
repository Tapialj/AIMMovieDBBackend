package com.aim.capstone.controller;

import java.util.*;

import com.aim.capstone.model.Actor;
import com.aim.capstone.model.Movie;
import com.aim.capstone.service.ActorService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/actors")
public class ActorController 
{
  
  private final ActorService actorService;


  @GetMapping
  public List<Actor> getActors()
  {
    return actorService.getActors();
  }

  @GetMapping(path = "{actorId}")
  public Actor getActor(@PathVariable Long actorId)
  {
    return actorService.getActor(actorId);
  }

  @GetMapping(path = "{actorId}/movies")
  public List<Movie> getActorMovies(@PathVariable Long actorId)
  {
    return actorService.getActorMovies(actorId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Actor createActor(@RequestBody Actor actor)
  {
    return actorService.addNewActor(actor);
  }

  @DeleteMapping(path = "{actorId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteActor(@PathVariable Long actorId)
  {
    actorService.deleteActor(actorId);
  }

  @PutMapping(path = "{actorId}")
  public Actor updateActor(@RequestBody Actor actor)
  {
    return actorService.updateActor(actor);
  }

}
