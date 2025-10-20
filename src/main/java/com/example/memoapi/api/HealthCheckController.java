package com.example.memoapi.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("안녕안녕 젠킨스 EKS 자동 배포~~~");
    }
}
