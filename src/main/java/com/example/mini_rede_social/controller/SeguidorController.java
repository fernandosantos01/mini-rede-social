package com.example.mini_rede_social.controller;

import com.example.mini_rede_social.service.SeguidorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/usuarios/{username}")
public class SeguidorController {

    private final SeguidorService seguidorService;

    public SeguidorController(SeguidorService seguidorService) {
        this.seguidorService = seguidorService;
    }
    @PostMapping("/seguir")
    public ResponseEntity<Void> seguir(@PathVariable String username) {
        seguidorService.seguir(username);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @DeleteMapping("/deixarDeSeguir")
    public ResponseEntity<Void> deixarDeSeguir(@PathVariable String username) {
        seguidorService.deixarSeguir(username);
        return ResponseEntity.noContent().build();
    }
}