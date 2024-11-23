package ex03.lab5.ies.lab5_3.services;

import ex03.lab5.ies.lab5_3.models.Message;

import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private KafkaTemplate<String, Object> template;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.template = kafkaTemplate;
    }

    private int nMec = 112981;

    public void produceMessages() {

        // Fibonacci sequence initialization
        int a = 0, b = 1;

        // Generate and send messages
        while (a <= nMec) {
            // Create a message
            Message message = new Message(nMec, a, "fibonacci");

            // Send the message
            sendToKafka(message);

            // Generate the next Fibonacci number
            int next = a + b;
            a = b;
            b = next;
        }
    }
    
    private ProducerRecord<String, Object> createRecord(Message data) {
        String topicName = "lab05_" + nMec;
        return new ProducerRecord<>(topicName, data);
    }

    public void sendToKafka(final Message data) {
        final ProducerRecord<String, Object> record = createRecord(data);
        
        CompletableFuture<SendResult<String, Object>> future = template.send(record);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                handleSuccess(data);
            } else {
                handleFailure(data, record, ex);
            }
        });
    }

    private void handleSuccess(Message data) {
        // Handle the success scenario
        System.out.println("Message sent successfully: " + data);
    }
    
    private void handleFailure(Message data, ProducerRecord<String, Object> record, Throwable ex) {
        // Handle the failure scenario
        System.err.println("Error sending message: " + data + ", due to: " + ex.getMessage());
    }
}
