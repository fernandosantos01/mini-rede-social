package com.example.mini_rede_social.controller;

import com.example.mini_rede_social.dto.ComentarioCriacaoDTO;
import com.example.mini_rede_social.dto.ComentarioResponseDTO;
import com.example.mini_rede_social.service.ComentarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ComentarioController {
    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @GetMapping("/postagens/{postId}/comentarios")
    public ResponseEntity<List<ComentarioResponseDTO>> listarComentariosDaPostagem(@PathVariable UUID postId) {
        return ResponseEntity.status(HttpStatus.OK).body(comentarioService.listarComentariosDaPostagem(postId));
    }

    @PostMapping("/postagens/{postId}/comentarios")
    public ResponseEntity<ComentarioResponseDTO> criarComentario(@PathVariable UUID postId, @RequestBody @Valid ComentarioCriacaoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(comentarioService.criarComentario(postId, dto));

    }

    @DeleteMapping("/comentario/{comentarioId}")
    public ResponseEntity<Void> deletarComentario(@PathVariable UUID comentarioId) {
        comentarioService.deletarComentario(comentarioId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
