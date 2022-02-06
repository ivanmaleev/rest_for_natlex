package ru.job4j.rest_for_natlex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RestForNatlexApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestForNatlexApplication.class, args);
    }
}
