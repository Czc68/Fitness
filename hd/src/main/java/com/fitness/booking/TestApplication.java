package com.fitness.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @GetMapping("/")
    public String home() {
        return "Hello, Fitness System is running!";
    }

    @GetMapping("/test")
    public String test() {
        return "Test endpoint is working!";
    }
} 