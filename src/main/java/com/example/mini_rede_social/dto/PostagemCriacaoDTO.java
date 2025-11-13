package com.example.mini_rede_social.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record PostagemCriacaoDTO(@NotNull(message = "Arquivo da imagem é obrigatório") MultipartFile imagem,
                                 String legenda) {
}
