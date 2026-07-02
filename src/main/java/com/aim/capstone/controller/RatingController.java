package com.aim.capstone.controller;

import java.util.*;

import com.aim.capstone.model.Rating;
import com.aim.capstone.service.RatingService;

import org.springframework.web.bind.annotation.*;

import lombok.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/ratings")
public class RatingController 
{

  private final RatingService ratingService;


  @GetMapping
  public List<Rating> getRatings()
  {
    return ratingService.getRatings();
  }
  
}
