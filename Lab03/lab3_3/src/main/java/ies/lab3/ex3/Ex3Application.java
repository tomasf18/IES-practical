package ies.lab3.ex3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "Movie Quotes API",
        version = "1.0.0",
        description = "Famous quotes from movies.",
        contact = @Contact(name = "Tomás Santos", email = "tomas@tomas.com")
    )
)
@SpringBootApplication
public class Ex3Application {

	public static void main(String[] args) {
		SpringApplication.run(Ex3Application.class, args);
	}

}
