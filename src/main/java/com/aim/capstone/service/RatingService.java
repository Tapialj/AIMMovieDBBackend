package com.aim.capstone.service;

import java.util.*;

import com.aim.capstone.model.Rating;
import com.aim.capstone.repository.RatingRepository;

import org.springframework.stereotype.Service;

import lombok.*;


@Service
@RequiredArgsConstructor
public class RatingService 
{
  
  private final RatingRepository ratingRepository;


  public List<Rating> getRatings()
  {
    return ratingRepository.findAll();
  }
}
