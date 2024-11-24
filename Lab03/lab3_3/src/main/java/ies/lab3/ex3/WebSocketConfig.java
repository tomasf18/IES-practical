package ies.lab3.ex3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${FRONTEND_IP}")
    private String frontendIp;

    @Value("${FRONTEND_PORT}")
    private String frontendPort;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enables simple broker for topics
        config.enableSimpleBroker("/topic"); // Clients can subscribe to "/topic/movies" and "/topic/quotes"

        // Specifies the prefix for messages that are sent from the client to the
        // server.
        // Clients will send messages with destinations like "/app/message", and the
        // server will route those messages to controller methods annotated with
        // @MessageMapping("/message").
        // config.setApplicationDestinationPrefixes("/app"); // Is not necessary since the received message don't come from clients but from Kafka consumer
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Clients connect to WebSocket at "/backend-ws"
        registry.addEndpoint("/backend-ws").setAllowedOrigins("http://" + frontendIp + ":" + frontendPort);
    }
}
