package ies.lab3.ex3;   

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

@Controller
@RequestMapping("api/quotes")
public class QuoteController {

    private QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping
    public ResponseEntity<Quote> createQuote(@RequestBody Quote quote){
        Quote savedQuote = quoteService.createQuote(quote);
        return new ResponseEntity<>(savedQuote, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Quote> getQuoteById(@PathVariable("id") Long quoteId){
        Quote quote = quoteService.getQuoteById(quoteId);
        return new ResponseEntity<>(quote, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllQuotes(@RequestParam(name = "movieName", required = false) String movieName) {
        if (movieName != null) {
            List<Quote> quotes = quoteService.getQuotesByMovie(movieName);
            return new ResponseEntity<>(quotes, HttpStatus.OK);
        }
        List<Quote> quotes = quoteService.getAllQuotes();
        return new ResponseEntity<>(quotes, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Quote> updateQuote(@PathVariable("id") Long quoteId, @RequestBody Quote quote) {
        quote.setId(quoteId);
        Quote updatedQuote = quoteService.updateQuote(quote);
        return new ResponseEntity<>(updatedQuote, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteQuote(@PathVariable("id") Long quoteId){
        quoteService.deleteQuote(quoteId);
        return new ResponseEntity<>("Quote successfully deleted!", HttpStatus.OK);
    }
}