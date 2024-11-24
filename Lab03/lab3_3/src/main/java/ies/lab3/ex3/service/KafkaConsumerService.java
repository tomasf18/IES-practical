package ies.lab3.ex3.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import ies.lab3.ex3.entity.Message;
import ies.lab3.ex3.entity.Movie;
import ies.lab3.ex3.entity.Quote;
import ies.lab3.ex3.controller.MessageController;

@Service
public class KafkaConsumerService {

    private final MovieService movieService;
    private final QuoteService quoteService;
    private final MessageController webSocketController;

    public KafkaConsumerService(MovieService movieService, QuoteService quoteService, MessageController webSocketController) {
        this.movieService = movieService;
        this.quoteService = quoteService;
        this.webSocketController = webSocketController;
    }

    @KafkaListener(topics = "quotes", groupId = "consumers_1")
    public void listen(Message message) {
        System.out.println("Received message: " + message.toString());

        // Process the movie and quote as usual
        Movie movie = new Movie(message.getMovieTitle(), message.getMovieYear());
        movieService.createMovie(movie);

        Quote quote = new Quote(message.getQuote(), movie);
        quoteService.createQuote(quote);

        // Send the message to all WebSocket clients, who are subscribed to the "/topic/movies" and "/topic/quotes" destinations
        webSocketController.sendMovie(movie);
        webSocketController.sendQuote(quote);

    }
}

