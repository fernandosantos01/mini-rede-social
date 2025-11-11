package com.example.mini_rede_social.controller;

import com.example.mini_rede_social.dto.CurtidaResponseDTO;
import com.example.mini_rede_social.dto.PerfilResponseDTO;
import com.example.mini_rede_social.service.CurtidaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CurtidaController {
    private final CurtidaService curtidaService;

    public CurtidaController(CurtidaService curtidaService) {
        this.curtidaService = curtidaService;
    }

    @PostMapping("/postagens/{postId}/curtir")
    public ResponseEntity<CurtidaResponseDTO> curtir(@PathVariable UUID postId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(curtidaService.curtirPostagem(postId));
    }

    @DeleteMapping("/postagens/{postId}/descurtir")
    public ResponseEntity<Void> descurtir(@PathVariable UUID postId) {
        curtidaService.descurtirPostagem(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
