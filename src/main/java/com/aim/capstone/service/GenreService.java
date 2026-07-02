package com.aim.capstone.service;

import java.util.*;

import com.aim.capstone.model.Genre;
import com.aim.capstone.repository.GenreRepository;

import org.springframework.stereotype.Service;

import lombok.*;


@Service
@RequiredArgsConstructor
public class GenreService 
{

  private final GenreRepository genreRepository;


  public List<Genre> getGenres()
  {
    return genreRepository.findAll();
  }

}
