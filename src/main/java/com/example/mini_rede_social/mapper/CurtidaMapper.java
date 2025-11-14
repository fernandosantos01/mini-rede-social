package com.example.mini_rede_social.mapper;

import com.example.mini_rede_social.dto.CurtidaResponseDTO;
import com.example.mini_rede_social.model.CurtidaModel;
import org.springframework.stereotype.Component;

@Component
public class CurtidaMapper {

    public CurtidaResponseDTO toResponseDTO(CurtidaModel curtida) {
        if (curtida == null) {
            return null;
        }

        return new CurtidaResponseDTO(
                curtida.getId(),
                curtida.getUsuario().getUsername(),
                curtida.getUsuario().getId(),
                curtida.getPostagem().getId()
        );
    }
}