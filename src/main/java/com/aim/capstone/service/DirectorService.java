package com.aim.capstone.service;

import java.util.*;

import com.aim.capstone.model.Director;
import com.aim.capstone.model.Movie;
import com.aim.capstone.repository.DirectorRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.*;


@Service
@RequiredArgsConstructor
public class DirectorService 
{
  
  private final DirectorRepository directorRepository;


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
    return directorRepository.findMovieByDirectorId(id);
  }

  public Director getRandomDirector()
  {
    return getRandom();
  }

  public List<Director> getRandomDirectors()
  {
    List<Director> randos = new ArrayList<Director>();

    do
    {
      Director testDirector = getRandom();

      if(!existsInList(randos, testDirector.getId()))
        randos.add(testDirector);
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

  private boolean existsInList(List<Director> list, long id)
  {
    return list.stream().anyMatch(a -> id == a.getId());
  }

  private Director getRandom()
  {
    long max = directorRepository.getMax();
    long randId;
    Optional<Director> temp = Optional.empty();

    do
    {
      randId = new Random().nextLong(max);
      temp = directorRepository.findById(randId);
    }
    while(temp.isEmpty());

    return temp.get();
  }

}
