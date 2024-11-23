package ex03.lab5.ies.lab5_3;

import ex03.lab5.ies.lab5_3.services.KafkaProducerService;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Lab53Application {

	public static void main(String[] args) {
		SpringApplication.run(Lab53Application.class, args);
	}


    @Bean
    public ApplicationRunner runner(KafkaProducerService producer) {
        return args -> {
            producer.produceMessages();
        };
    }
}
