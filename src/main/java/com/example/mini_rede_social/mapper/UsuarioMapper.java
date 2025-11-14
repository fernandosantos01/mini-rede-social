package com.example.mini_rede_social.mapper;

import com.example.mini_rede_social.dto.RegistroCompletoDTO;
import com.example.mini_rede_social.dto.UsuarioResponseDTO;
import com.example.mini_rede_social.model.PerfilModel;
import com.example.mini_rede_social.model.UsuarioModel;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    /**
     * Converte o DTO de registro para a Entidade UsuarioModel.
     * (A senha será definida no Service, pois criptografia é regra de negócio).
     */
    public UsuarioModel toUsuarioModel(RegistroCompletoDTO dto) {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setUsername(dto.username());
        usuario.setEmail(dto.email());
        usuario.setPhone_number(dto.phone_number());
        return usuario;
    }

    /**
     * Converte o DTO de registro para a Entidade PerfilModel.
     */
    public PerfilModel toPerfilModel(RegistroCompletoDTO dto) {
        PerfilModel perfil = new PerfilModel();
        perfil.setNomeCompleto(dto.nomeCompleto());
        perfil.setDataNascimento(dto.dataNascimento());
        perfil.setBio(dto.bio());
        return perfil;
    }

    /**
     * Converte a Entidade UsuarioModel em um DTO de Resposta seguro (sem senha).
     */
    public UsuarioResponseDTO toResponseDTO(UsuarioModel usuario) {
        if (usuario == null) {
            return null;
        }

        PerfilModel perfil = usuario.getPerfil();
        if (perfil == null) {
            return new UsuarioResponseDTO(
                    usuario.getId(),
                    usuario.getUsername(),
                    usuario.getEmail(),
                    usuario.getPhone_number(),
                    null, null, null
            );
        }

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getPhone_number(),
                perfil.getNomeCompleto(),
                perfil.getBio(),
                perfil.getDataNascimento()
        );
    }
}