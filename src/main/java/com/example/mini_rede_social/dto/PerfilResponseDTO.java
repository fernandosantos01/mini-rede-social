package com.example.mini_rede_social.dto;

import java.time.LocalDate;
import java.util.UUID;

public record PerfilResponseDTO(UUID id,
                                String nomeCompleto,
                                String bio,
                                LocalDate dataNascimento) {
}
