package ies.lab3.ex3;

import java.util.List;

public interface MovieService {
    Movie createMovie(Movie movie);
    Movie getMovieById(Long id);
    List<Movie> getMoviesByYear(String year);
    List<Movie> getAllMovies();
    Movie updateMovie(Movie movie);
    void deleteMovie(Long id);
}
