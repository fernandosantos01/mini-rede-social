package com.example.mini_rede_social.mapper;

import com.example.mini_rede_social.dto.AutorDTO;
import com.example.mini_rede_social.dto.PostagemAtualizacaoDTO;
import com.example.mini_rede_social.dto.PostagemResponseDTO;
import com.example.mini_rede_social.model.PostagemModel;
import org.springframework.stereotype.Component;

@Component
public class PostagemMapper {

    public PostagemResponseDTO toResponseDTO(PostagemModel entity) {
        if (entity == null) return null;

        AutorDTO autorDTO = new AutorDTO(
                entity.getUsuario().getId(),
                entity.getUsuario().getUsername()
        );

        return new PostagemResponseDTO(
                entity.getId(),
                entity.getConteudoUrl(),
                entity.getLegenda(),
                entity.getDataCriacao(),
                autorDTO
        );
    }

    public void updateEntityFromDto(PostagemAtualizacaoDTO dto, PostagemModel entity) {
        if (dto.legenda() != null) {
            entity.setLegenda(dto.legenda());
        }
    }
}
