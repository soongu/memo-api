package com.example.memoapi.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Hello World Kubernetes and Jenkins!!");
    }
}
