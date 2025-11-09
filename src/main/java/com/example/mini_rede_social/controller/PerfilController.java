package com.example.mini_rede_social.controller;

import com.example.mini_rede_social.dto.PerfilAtualizacaoDTO;
import com.example.mini_rede_social.model.PerfilModel;
import com.example.mini_rede_social.service.PerfilService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfis")
public class PerfilController {
    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<PerfilModel> getPerfilPorUsername(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body(perfilService.buscarPerfilPorUsername(username));
    }

    @PutMapping
    public ResponseEntity<PerfilModel> atualizarPerfil(@RequestBody @Valid PerfilAtualizacaoDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(perfilService.atualizarPerfil(dto));
    }
}
