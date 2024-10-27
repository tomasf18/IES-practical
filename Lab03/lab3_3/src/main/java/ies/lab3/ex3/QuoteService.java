package ies.lab3.ex3;

import java.util.List;

public interface QuoteService {
    Quote createQuote(Quote quote);
    Quote getQuoteById(Long id);
    List<Quote> getQuotesByMovie(String movieName);
    List<Quote> getAllQuotes();
    Quote updateQuote(Quote quote);
    void deleteQuote(Long id);
}

