package org.gycoding.messages;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.gycoding"})
public class ChatApplication {
    public static void main(String[] args) {
        try {
            Dotenv dotenv = Dotenv.load();
            dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        } catch(Exception e) {
            System.out.println("Error loading .env file.");
        } finally {
            SpringApplication.run(ChatApplication.class, args);
        }
    }
}
