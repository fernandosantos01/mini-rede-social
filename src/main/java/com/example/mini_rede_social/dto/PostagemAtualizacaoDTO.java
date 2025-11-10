package com.example.mini_rede_social.dto;

import org.springframework.web.multipart.MultipartFile;

public record PostagemAtualizacaoDTO(MultipartFile imagem,
                                     String legenda) {
}
