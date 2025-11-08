package com.example.mini_rede_social.dto;

import java.time.LocalDate;
import java.util.UUID;

public record UsuarioResponseDTO(
        UUID id,
        String username,
        String email,
        String phone_number,
        String nomeCompleto,
        String bio,
        LocalDate dataNascimento
) {}
