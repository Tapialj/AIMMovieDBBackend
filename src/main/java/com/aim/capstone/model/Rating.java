package com.aim.capstone.model;

import java.util.*;
import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.*;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ratings")
public class Rating implements Comparable<Rating>
{
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "rating_id")
  private Long id;
  @Column(name = "rating")
  private String rating;
  @OneToMany(mappedBy = "rating")
  @JsonIgnore
  private List<Movie> movies;
  
  
  @Override
  public int compareTo(Rating r)
  {
    return this.getId().compareTo(r.getId());
  }
  
}
