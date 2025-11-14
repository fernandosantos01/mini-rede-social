package com.example.mini_rede_social.controller;

import com.example.mini_rede_social.dto.PerfilResponseDTO;
import com.example.mini_rede_social.service.SeguidorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.PanelUI;
import java.util.List;


@RestController
@RequestMapping("/api/usuarios/{username}")
public class SeguidorController {

    private final SeguidorService seguidorService;

    public SeguidorController(SeguidorService seguidorService) {
        this.seguidorService = seguidorService;
    }

    @GetMapping("/seguidores")
    public ResponseEntity<List<PerfilResponseDTO>> getSeguidores(@PathVariable String username) {
        List<PerfilResponseDTO> seguidores = seguidorService.listarSeguidores(username);
        return ResponseEntity.ok(seguidores);
    }
    @GetMapping("/seguindo")
    public ResponseEntity<List<PerfilResponseDTO>> getSeguindo(@PathVariable String username) {
        List<PerfilResponseDTO> seguindo = seguidorService.listarSeguindo(username);
        return ResponseEntity.ok(seguindo);
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