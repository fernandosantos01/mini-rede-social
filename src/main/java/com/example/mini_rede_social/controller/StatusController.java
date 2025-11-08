package com.example.mini_rede_social.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StatusController {
    @GetMapping("/status")
    public ResponseEntity<Object> getStatus() {
        return ResponseEntity.status(HttpStatus.OK).body("status: API NO AR!");
    }
}
