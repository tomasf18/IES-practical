package ex4.lab2.ies.deti.ua.quotes_api;

import org.springframework.stereotype.Service;

import ex4.lab2.ies.deti.ua.quotes_api.Show;
import ex4.lab2.ies.deti.ua.quotes_api.Quote;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
public class QuoteService {
    private static final List<Show> shows = List.of(
        new Show(1, "Star Wars"),
        new Show(2, "Titanic"),
        new Show(3, "Avengers: Endgame"),
        new Show(4, "Scarface")
    );

    private static final List<Quote> quotes = List.of(
        new Quote(1, "Star Wars", "May the Force be with you."),
        new Quote(2, "Titanic", "I'm the king of the world!"),
        new Quote(3, "Avengers: Endgame", "I am Iron Man."),
        new Quote(4, "Scarface", "Say hello to my little friend!"),
        new Quote(5, "Star Wars", "I find your lack of faith disturbing."),
        new Quote(6, "Titanic", "I'll never let go, Jack. I promise.")
    );
    
    public List<Show> getShows() {
        return shows;
    }

    public Quote getRandomQuote() {
        return quotes.get((int) (Math.random() * quotes.size()));
    }

    public List<Quote> getQuotesByShowId(int showId) {
        List<Show> shows = this.getShows();
        Show choice = shows.stream().filter(show -> show.id() == showId).findFirst().orElse(null);
        if (choice == null) {
            return null;
        }

        return quotes.stream().filter(quote -> quote.show_name().equals(choice.name())).collect(Collectors.toList());
    }
}
