package com.aim.capstone.model;

import java.util.*;
import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.*;

import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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
  @ManyToMany(mappedBy = "directors")
  @JsonIgnore
  @Builder.Default
  @EqualsAndHashCode.Exclude
  private Set<Movie> movies = new HashSet<Movie>();
  
  
  @PreRemove
  private void removeActorFromMovies()
  {
    for (Movie m : this.movies)
    {
      m.removeDirector(this);
    }
  }
  
  @Override
  public int compareTo(Director d)
  {
    return this.getId().compareTo(d.getId());
  }

}
