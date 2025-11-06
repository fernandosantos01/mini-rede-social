package com.example.mini_rede_social.controller;

import com.example.mini_rede_social.dto.PostagemCriacaoAtualizacaoDTO;
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
    public ResponseEntity<?> criarPostagem(@Validated @ModelAttribute PostagemCriacaoAtualizacaoDTO dto
    ) {
        try {
            PostagemModel novaPostagem = postagemService.criarPostagem(dto);

            return ResponseEntity.status(HttpStatus.CREATED).body(novaPostagem);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao processar o arquivo de imagem: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha no upload para o Supabase: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<PostagemResponseDTO>> buscarTodasPostagens() {
        return ResponseEntity.status(HttpStatus.OK).body(postagemService.buscarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostagemModel> buscarPostagemPorId(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(postagemService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarPostagem(@PathVariable UUID id, @ModelAttribute @Valid PostagemCriacaoAtualizacaoDTO dto) {
        try {
            PostagemModel postagemAtualizada = postagemService.atualizarPostagem(id, dto);
            return ResponseEntity.status(HttpStatus.OK).body(postagemAtualizada);

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarPostagem(@PathVariable UUID id) {
        try {
            postagemService.deletarPostagem(id);

            return ResponseEntity.noContent().build();

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
