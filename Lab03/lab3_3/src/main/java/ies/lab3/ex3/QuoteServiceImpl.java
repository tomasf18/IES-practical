package ies.lab3.ex3;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class QuoteServiceImpl implements QuoteService {
    private QuoteRepository quoteRepository;

    public QuoteServiceImpl(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
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
    public List<Quote> getQuotesByMovie(String movieName) {
        List<Quote> quotes = quoteRepository.findQuotesByMovie(movieName);
        return quotes;
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
