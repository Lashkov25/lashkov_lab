package ua.com.reactive.lashkov_lab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableR2dbcRepositories
public class LashkovLabApplication {

    public static void main(String[] args) {
        SpringApplication.run(LashkovLabApplication.class, args);
    }
}
