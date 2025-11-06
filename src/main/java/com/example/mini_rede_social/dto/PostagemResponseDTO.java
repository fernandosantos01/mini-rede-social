package com.example.mini_rede_social.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostagemResponseDTO(UUID id,
                                  String conteudoUrl,
                                  String legenda,
                                  LocalDateTime dataCriacao,
                                  AutorDTO autorDTO) {
}
