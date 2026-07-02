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
@Table(name = "genres")
public class Genre implements Comparable<Genre>
{
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "genre_id")
  private Long id;
  @Column(name = "genre")
  private String genre;
  @OneToMany(mappedBy = "genre")//, fetch = FetchType.LAZY)
  @JsonIgnore
  private List<Movie> movies;


  @Override
  public int compareTo(Genre g)
  {
    return this.getId().compareTo(g.getId());
  }

}
