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
@Table(name = "directors")
public class Director implements Comparable<Director>
{
  
  @Id 
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "director_id")
  private Long id;
  @Column(name = "last_name")
  private String lastName;
  @Column(name = "first_name")
  private String firstName;
  @OneToMany(mappedBy = "director")
  @JsonIgnore
  private List<Movie> movies;
  // @OneToMany(mappedBy = "director", cascade = CascadeType.ALL)
  // private List<Comment> comments;
  
  
  @Override
  public int compareTo(Director d)
  {
    return this.getId().compareTo(d.getId());
  }

}
