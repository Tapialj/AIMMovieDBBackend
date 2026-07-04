package com.aim.capstone.model;

import java.time.*;
import java.util.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.*;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movies")
public class Movie implements Comparable<Movie>
{
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "movie_id")
  private Long id;
  @Column(name = "title")
  private String title;
  @Column(name = "movie_length")
  private int movieLength;
  @Column(name = "release_date")
  private LocalDate releaseDate;
  @Column(name = "trailer_url")
  private String trailerUrl;
  @ManyToOne
  @JoinColumn(name = "genre_id", nullable = false)
  private Genre genre;
  @ManyToOne
  @JoinColumn(name = "rating_id", nullable = false)
  private Rating rating;
  @ManyToOne
  @JoinColumn(name = "director_id")
  private Director director;
  @ManyToMany //(fetch = FetchType.LAZY)//cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinTable(
    name = "movie_cast", 
    joinColumns = @JoinColumn(name = "movie_id"),
    inverseJoinColumns = @JoinColumn(name = "actor_id")
  )
  @Fetch(FetchMode.JOIN)
  private List<Actor> actors;
  // @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
  //@ManyToAny()
  // private List<Comment> comments;
  
  
  @Override
  public int compareTo(Movie m)
  {
    return this.getId().compareTo(m.getId());
  }
  
}