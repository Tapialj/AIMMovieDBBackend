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
@Table(name = "actors")
public class Actor implements Comparable<Actor>
{
  
  @Id 
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "actor_id")
  private Long id;
  @Column(name = "last_name")
  private String lastName;
  @Column(name = "first_name")
  private String firstName;
  @ManyToMany(mappedBy = "actors")//, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JsonIgnore
  private List<Movie> movies;
  // @OneToMany(mappedBy = "actor", cascade = CascadeType.ALL)
  // private List<Comment> comments;


  @Override
  public int compareTo(Actor a)
  {
    return this.getId().compareTo(a.getId());
  }

}
