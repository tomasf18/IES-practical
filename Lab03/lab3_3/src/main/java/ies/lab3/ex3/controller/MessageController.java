package ies.lab3.ex3.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ies.lab3.ex3.entity.Movie;
import ies.lab3.ex3.entity.Quote;

@Controller
public class MessageController {

    // This methods are called when a message is received in the kafka consumer,
    // and then it is sent to all clients (eg.: frontend, etc. - all
    // those who have websockets connections and are subscribed
    // to the "/topic/movies" and "/topic/quotes" destinations)

    // @MessageMapping("/message"), which is the destination prefix for receiving
    // messages, and @SendTo(...)
    // are not necessary here since the received message don't come from clients but
    // from
    // Kafka consumer (which receives from the Kafka topic to which it is
    // subscribed)
    // So, my clients (frontend) are not sending messages /message, but only
    // receiving them
    private final SimpMessagingTemplate messagingTemplate;

    public MessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMovie(Movie movie) {
        // Broadcast the movie to all subscribers of "/topic/movies"
        messagingTemplate.convertAndSend("/topic/movies", movie);
    }

    public void sendQuote(Quote quote) {
        // Broadcast the quote to all subscribers of "/topic/quotes"
        messagingTemplate.convertAndSend("/topic/quotes", quote);
        // Convert the given Object to serialized form, possibly using a
        // org.springframework.messaging.converter.MessageConverter, wrap it as a
        // message and send it to the given destination.

    }

}
