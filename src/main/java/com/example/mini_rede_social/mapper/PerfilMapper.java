package com.example.mini_rede_social.mapper;

import com.example.mini_rede_social.dto.PerfilResponseDTO;
import com.example.mini_rede_social.model.PerfilModel;
import com.example.mini_rede_social.model.UsuarioModel;
import org.springframework.stereotype.Component;


@Component
public class PerfilMapper {

    /**
     * Converte a Entidade PerfilModel em um DTO de Resposta.
     */
    public PerfilResponseDTO toResponseDTO(PerfilModel perfil) {
        if (perfil == null) {
            return null;
        }

        UsuarioModel usuario = perfil.getUsuario();

        return new PerfilResponseDTO(
                usuario.getId(),
                perfil.getNomeCompleto(),
                perfil.getBio(),
                perfil.getDataNascimento()
        );
    }
}