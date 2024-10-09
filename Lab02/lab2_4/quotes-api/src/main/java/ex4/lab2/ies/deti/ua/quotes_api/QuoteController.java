package ex4.lab2.ies.deti.ua.quotes_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import ex4.lab2.ies.deti.ua.quotes_api.Show;
import ex4.lab2.ies.deti.ua.quotes_api.QuoteService;
import ex4.lab2.ies.deti.ua.quotes_api.Quote;

@RestController
@RequestMapping("/api")
public class QuoteController {

	@Autowired
	private QuoteService service;

	@GetMapping("/quote")
	public Quote randomQuote() {
		return service.getRandomQuote();
	}

	@GetMapping("/shows")
	public List<Show> shows() {
		return service.getShows();
	}

	@GetMapping("/quotes")
	public List<Quote> quotes(@RequestParam("show_id") String show_id) {
		return service.getQuotesByShowId(Integer.parseInt(show_id));
	}

}