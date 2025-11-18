package com.example.mini_rede_social.controller;

import com.example.mini_rede_social.dto.PerfilAtualizacaoDTO;
import com.example.mini_rede_social.dto.PerfilResponseDTO;
import com.example.mini_rede_social.model.PerfilModel;
import com.example.mini_rede_social.service.PerfilService;
import com.example.mini_rede_social.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/perfis")
public class PerfilController {
    private final PerfilService perfilService;
    private final UsuarioService usuarioService;

    public PerfilController(PerfilService perfilService, UsuarioService usuarioService) {
        this.perfilService = perfilService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<PerfilResponseDTO> getPerfilPorUsername(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body(perfilService.buscarPerfilPorUsername(username));
    }

    @PutMapping
    public ResponseEntity<PerfilResponseDTO> atualizarPerfil(@RequestBody @Valid PerfilAtualizacaoDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(perfilService.atualizarPerfil(dto));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deletarMinhaConta() {
        usuarioService.deletarMinhaContaLogada();

        return ResponseEntity.noContent().build();
    }
}
