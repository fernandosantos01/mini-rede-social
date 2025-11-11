package com.example.mini_rede_social.dto;

import java.util.UUID;

public record CurtidaResponseDTO(
        UUID id,
        String username,
        UUID usuarioId,
        UUID postagemId
) {}
