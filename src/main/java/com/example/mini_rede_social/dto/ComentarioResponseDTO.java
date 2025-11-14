package com.example.mini_rede_social.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ComentarioResponseDTO(
        UUID id,
        String texto,
        LocalDateTime dataCriacao,
        AutorDTO autor,
        UUID postagemId
) {
}
