package com.example.mini_rede_social.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ComentarioCriacaoDTO(@NotBlank(message = "O comentário não pode ser vazio")
                                   @Size(max = 500, message = "Comentário muito longo (máx 500 caracteres)")
                                   String texto) {
}
