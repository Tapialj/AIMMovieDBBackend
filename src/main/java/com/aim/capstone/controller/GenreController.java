package com.aim.capstone.controller;

import java.util.*;

import com.aim.capstone.model.Genre;
import com.aim.capstone.service.GenreService;

import org.springframework.web.bind.annotation.*;

import lombok.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/genres")
public class GenreController 
{
  
  private final GenreService genreService;


  @GetMapping
  public List<Genre> getGenres()
  {
    return genreService.getGenres();
  }

}
