package ex03.lab5.ies.lab5_3.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import ex03.lab5.ies.lab5_3.models.Message;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "lab05_112981", groupId = "consumers_1")
    public void listen(Message message) {
        System.out.println("Received message: " + message.toString());
    }
}
