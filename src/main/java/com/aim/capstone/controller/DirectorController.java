package com.aim.capstone.controller;

import java.util.*;

import com.aim.capstone.model.Director;
import com.aim.capstone.model.Movie;
import com.aim.capstone.service.DirectorService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/directors")
public class DirectorController 
{
  
  private final DirectorService directorService;


  @GetMapping
  public List<Director> getDirectors()
  {
    return directorService.getDirectors();
  }

  @GetMapping(path = "{directorId}")
  public Director getDirector(@PathVariable Long directorId)
  {
    return directorService.getDirector(directorId);
  }

  @GetMapping(path = "{directorId}/movies")
  public List<Movie> getDirectorMovies(@PathVariable Long directorId)
  {
    return directorService.getDirectorMovies(directorId);
  }

  @GetMapping(path = "random")
  public Director getRandomDirector()
  {
    return directorService.getRandomDirector();
  }

  @GetMapping(path = "random-list")
  public List<Director> getRandomDirectors()
  {
    return directorService.getRandomDirectors();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Director createDirector(@RequestBody Director director)
  {
    return directorService.addNewDirector(director);
  }

  @DeleteMapping(path = "{directorId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteDirector(@PathVariable Long directorId)
  {
    directorService.deleteDirector(directorId);
  }

  @PutMapping(path = "{directorId}")
  public Director updateDirector(@RequestBody Director director)
  {
    return directorService.updateDirector(director);
  }

}
