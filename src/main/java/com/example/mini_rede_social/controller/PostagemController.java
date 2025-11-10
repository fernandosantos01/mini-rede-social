package com.example.mini_rede_social.controller;

import com.example.mini_rede_social.dto.PostagemAtualizacaoDTO;
import com.example.mini_rede_social.dto.PostagemCriacaoDTO;
import com.example.mini_rede_social.dto.PostagemResponseDTO;
import com.example.mini_rede_social.model.PostagemModel;
import com.example.mini_rede_social.service.PostagemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/postagens")
public class PostagemController {

    private final PostagemService postagemService;

    public PostagemController(PostagemService postagemService) {
        this.postagemService = postagemService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> criarPostagem(@Validated @ModelAttribute PostagemCriacaoDTO dto
    ) throws IOException {
        PostagemResponseDTO novaPostagem = postagemService.criarPostagem(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaPostagem);
    }

    @GetMapping
    public ResponseEntity<List<PostagemResponseDTO>> buscarTodasPostagens() {
        return ResponseEntity.status(HttpStatus.OK).body(postagemService.buscarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostagemResponseDTO> buscarPostagemPorId(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(postagemService.buscarPorId(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> atualizarPostagem(
            @PathVariable UUID id,
            @ModelAttribute @Validated PostagemAtualizacaoDTO dto) throws IOException {
            PostagemResponseDTO postagemAtualizada = postagemService.atualizarPostagem(id, dto);
            return ResponseEntity.status(HttpStatus.OK).body(postagemAtualizada);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarPostagem(@PathVariable UUID id) {
        postagemService.deletarPostagem(id);
        return ResponseEntity.noContent().build();
    }
}
