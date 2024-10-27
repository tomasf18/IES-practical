package ies.lab3.ex3;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl implements MovieService {
    private MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    @Override
    public List<Movie> getMoviesByYear(String year) {
        List<Movie> movies = movieRepository.findMoviesByYear(year);
        return movies;
    }
    
    @Override
    public List<Movie> getAllMovies() {
        return (List<Movie>) movieRepository.findAll();
    }

    @Override
    public Movie updateMovie(Movie movie) {
        Movie movieToUpdate = movieRepository.findById(movie.getId()).get();
        movieToUpdate.setTitle(movie.getTitle());
        movieToUpdate.setYear(movie.getYear());
        Movie updatedMovie = movieRepository.save(movieToUpdate);
        return updatedMovie;
    }

    @Override
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }
    
}
