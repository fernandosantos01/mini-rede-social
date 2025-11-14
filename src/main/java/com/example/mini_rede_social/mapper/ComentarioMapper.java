package com.example.mini_rede_social.mapper;

import com.example.mini_rede_social.dto.AutorDTO;
import com.example.mini_rede_social.dto.ComentarioResponseDTO;
import com.example.mini_rede_social.model.ComentarioModel;
import org.springframework.stereotype.Component;

@Component
public class ComentarioMapper {

    public ComentarioResponseDTO toResponseDTO(ComentarioModel entity) {
        if (entity == null) return null;

        AutorDTO autorDTO = new AutorDTO(
                entity.getUsuario().getId(),
                entity.getUsuario().getUsername()
        );

        return new ComentarioResponseDTO(
                entity.getId(),
                entity.getTexto(),
                entity.getDataCriacao(),
                autorDTO,
                entity.getPostagem().getId()
        );
    }
}