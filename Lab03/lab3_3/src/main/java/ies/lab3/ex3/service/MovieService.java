package ies.lab3.ex3.service;

import java.util.List;

import ies.lab3.ex3.entity.Movie;

public interface MovieService {
    Movie createMovie(Movie movie);
    Movie getMovieById(Long id);
    List<Movie> getMoviesByYear(String year);
    List<Movie> getAllMovies();
    Movie updateMovie(Movie movie);
    void deleteMovie(Long id);
}
