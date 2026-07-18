package com.aim.capstone.model;

import java.time.*;
import java.util.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.*;

import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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
  @Fetch(FetchMode.JOIN)
  private Genre genre;
  @ManyToOne
  @JoinColumn(name = "rating_id", nullable = false)
  @Fetch(FetchMode.JOIN)
  private Rating rating;
  @ManyToMany
  @JoinTable(
    name = "movie_directors", 
    joinColumns = @JoinColumn(name = "movie_id"),
    inverseJoinColumns = @JoinColumn(name = "director_id")
  )
  @Fetch(FetchMode.JOIN)
  @Builder.Default
  @EqualsAndHashCode.Exclude
  private Set<Director> directors = new HashSet<Director>();;
  @ManyToMany
  @JoinTable(
    name = "movie_cast", 
    joinColumns = @JoinColumn(name = "movie_id"),
    inverseJoinColumns = @JoinColumn(name = "actor_id")
  )
  @Fetch(FetchMode.JOIN)
  @Builder.Default
  @EqualsAndHashCode.Exclude
  private Set<Actor> actors = new HashSet<Actor>();


  public void addActor(Actor actor)
  {
    actors.add(actor);
    actor.getMovies().add(this);
  }
  
  public void removeActor(Actor actor)
  {
    actors.remove(actor);
    actor.getMovies().remove(this);
  }

  public void addDirector(Director director)
  {
    directors.add(director);
    director.getMovies().add(this);
  }
  
  public void removeDirector(Director director)
  {
    directors.remove(director);
    director.getMovies().remove(this);
  }

  @Override
  public int compareTo(Movie m)
  {
    return this.getId().compareTo(m.getId());
  }
  
}