package ies.lab3.ex3.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ies.lab3.ex3.entity.Movie;
import ies.lab3.ex3.entity.Quote;
import ies.lab3.ex3.repository.MovieRepository;
import ies.lab3.ex3.repository.QuoteRepository;

@Service
public class QuoteServiceImpl implements QuoteService {
    private QuoteRepository quoteRepository;
    private MovieRepository movieRepository;

    public QuoteServiceImpl(QuoteRepository quoteRepository, MovieRepository movieRepository) {
        this.quoteRepository = quoteRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public Quote createQuote(Quote quote) {
        return quoteRepository.save(quote);
    }

    @Override
    public Quote getQuoteById(Long id) {
        return quoteRepository.findById(id).orElse(null);
    }

    @Override
    public Movie getMovieById(Long movieId) {
        return movieRepository.findById(movieId).orElse(null); // New method to retrieve movie by ID
    }

    @Override
    public List<Quote> getQuotesByMovieId(Long movieId) {
        return quoteRepository.findByMovie_Id(movieId); // Fetch quotes by movieId
    }
    
    @Override
    public List<Quote> getAllQuotes() {
        return (List<Quote>) quoteRepository.findAll();
    }

    @Override
    public Quote updateQuote(Quote quote) {
        Quote quoteToUpdate = quoteRepository.findById(quote.getId()).get();
        quoteToUpdate.setQuote(quote.getQuote());
        quoteToUpdate.setMovie(quote.getMovie());
        Quote updatedQuote = quoteRepository.save(quoteToUpdate);
        return updatedQuote;
    }

    @Override
    public void deleteQuote(Long id) {
        quoteRepository.deleteById(id);
    }
    
}
