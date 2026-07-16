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
  @ManyToMany(mappedBy = "actors") //, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JsonIgnore
  @Builder.Default
  @EqualsAndHashCode.Exclude
  private Set<Movie> movies = new HashSet<Movie>();
  // @OneToMany(mappedBy = "actor", cascade = CascadeType.ALL)
  // private List<Comment> comments;
  

  @PreRemove
  private void removeActorFromMovies()
  {
    for (Movie m : this.movies)
    {
      m.removeActor(this);
    }
  }

  @Override
  public int compareTo(Actor a)
  {
    return this.getId().compareTo(a.getId());
  }

}
