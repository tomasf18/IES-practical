package ies.lab3.ex3.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ies.lab3.ex3.entity.Movie;
import ies.lab3.ex3.service.MovieService;

@Controller
@RequestMapping("api/v1/movies")
public class MovieController {

    private MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie){
        Movie savedMovie = movieService.createMovie(movie);
        return new ResponseEntity<>(savedMovie, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable("id") Long movieId){
        Movie movie = movieService.getMovieById(movieId);
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllMovies(@RequestParam(name = "year", required = false) String year) {
        if (year != null) {
            List<Movie> movies = movieService.getMoviesByYear(year);
            return new ResponseEntity<>(movies, HttpStatus.OK);
        }
        List<Movie> movies = movieService.getAllMovies();
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable("id") Long movieId, @RequestBody Movie movie) {
        movie.setId(movieId);
        Movie updatedMovie = movieService.updateMovie(movie);
        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable("id") Long movieId){
        movieService.deleteMovie(movieId);
        return new ResponseEntity<>("Movie successfully deleted!", HttpStatus.OK);
    }
}