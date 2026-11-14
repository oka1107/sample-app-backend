package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Sample App Backend is running!";
    }

    @GetMapping("/api/health")
    public Map<String, String> health() {
        return Map.of(
            "status", "OK",
            "message", "API connection successful"
        );
    }
}
