package ies.lab3.ex3.controller;   

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ies.lab3.ex3.dto.QuoteRequest;
import ies.lab3.ex3.entity.Movie;
import ies.lab3.ex3.entity.Quote;
import ies.lab3.ex3.service.QuoteService;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:5173")
public class QuoteController {

    private QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping("/quotes")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createQuote(@RequestBody QuoteRequest quoteRequest) {
        try {
            Movie movie = quoteService.getMovieById(quoteRequest.getMovieId());
            if (movie == null) {
                throw new IllegalArgumentException("Invalid movieId: " + quoteRequest.getMovieId());
            }

            Quote quote = new Quote(quoteRequest.getQuote(), movie);
            Quote savedQuote = quoteService.createQuote(quote);

            return new ResponseEntity<>(savedQuote, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/quotes/{id}")
    public ResponseEntity<Quote> getQuoteById(@PathVariable("id") Long quoteId){
        Quote quote = quoteService.getQuoteById(quoteId);
        return new ResponseEntity<>(quote, HttpStatus.OK);
    }

    @GetMapping("/quotes")
    public ResponseEntity<?> getAllQuotes(@RequestParam(name = "movieId", required = false) String movieId) {
        if (movieId != null) {
            try {
                Long movieIdLong = Long.parseLong(movieId);  
                List<Quote> quotes = quoteService.getQuotesByMovieId(movieIdLong);
                return new ResponseEntity<>(quotes, HttpStatus.OK);
            } catch (NumberFormatException e) {
                return new ResponseEntity<>("Invalid movieId format", HttpStatus.BAD_REQUEST);
            }
        }
        List<Quote> quotes = quoteService.getAllQuotes();
        return new ResponseEntity<>(quotes, HttpStatus.OK);
    }

    // random quote
    @GetMapping("/quote")
    public ResponseEntity<?> getRandomQuote() {
        List<Quote> quotes = quoteService.getAllQuotes();
        return new ResponseEntity<>(quotes.get((int) (Math.random() * quotes.size())), HttpStatus.OK);
    }

    @PutMapping("/quotes/{id}")
    public ResponseEntity<?> updateQuote(@PathVariable("id") Long quoteId, @RequestBody QuoteRequest quoteRequest) {
        try {
            Quote existingQuote = quoteService.getQuoteById(quoteId);
            if (existingQuote == null) {
                return new ResponseEntity<>("Quote not found", HttpStatus.NOT_FOUND);
            }

            Movie movie = quoteService.getMovieById(quoteRequest.getMovieId());
            if (movie == null) {
                throw new IllegalArgumentException("Invalid movieId: " + quoteRequest.getMovieId());
            }

            existingQuote.setQuote(quoteRequest.getQuote());
            existingQuote.setMovie(movie);

            Quote updatedQuote = quoteService.updateQuote(existingQuote);
            return new ResponseEntity<>(updatedQuote, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    

    @DeleteMapping("/quotes/{id}")
    public ResponseEntity<String> deleteQuote(@PathVariable("id") Long quoteId){
        quoteService.deleteQuote(quoteId);
        return new ResponseEntity<>("Quote successfully deleted!", HttpStatus.OK);
    }
}