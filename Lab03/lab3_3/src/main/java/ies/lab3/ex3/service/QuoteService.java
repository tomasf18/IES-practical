package ies.lab3.ex3.service;

import java.util.List;

import ies.lab3.ex3.entity.Quote;

public interface QuoteService {
    Quote createQuote(Quote quote);
    Quote getQuoteById(Long id);
    List<Quote> getQuotesByMovieId(Long movieId);
    List<Quote> getAllQuotes();
    Quote updateQuote(Quote quote);
    void deleteQuote(Long id);
}

