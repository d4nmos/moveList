package com.example.demo.repository;

import com.example.demo.models.Director;
import com.example.demo.models.Genre;
import com.example.demo.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findByName(String name);
    List<Movie> findByDirector(Director director);
    List<Movie> findByGenre(Genre genre);
    List<Movie> findByNameContaining(String searchName);
}
