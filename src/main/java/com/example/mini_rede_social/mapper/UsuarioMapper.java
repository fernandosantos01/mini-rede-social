package com.example.mini_rede_social.mapper;

import com.example.mini_rede_social.dto.RegistroCompletoDTO;
import com.example.mini_rede_social.dto.UsuarioResponseDTO;
import com.example.mini_rede_social.model.PerfilModel;
import com.example.mini_rede_social.model.UsuarioModel;
import org.springframework.stereotype.Component;
import java.time.LocalDate; // Importe (se seu DTO usar)

@Component
public class UsuarioMapper {

    /**
     * Converte o DTO de registro para a Entidade UsuarioModel.
     */
    public UsuarioModel toUsuarioModel(RegistroCompletoDTO dto) {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setUsername(dto.username());
        usuario.setEmail(dto.email());
        usuario.setPhone_number(dto.phone_number());
        // A senha é definida no Service
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
     * ⭐️ O ÚNICO MÉTODO DE RESPOSTA ⭐️
     * Converte o Usuario E o Perfil (que o Service buscou) em um DTO seguro.
     */
    public UsuarioResponseDTO toResponseDTO(UsuarioModel usuario, PerfilModel perfil) {
        if (usuario == null) {
            return null;
        }

        // Garante que o perfil não é nulo (embora o service deva garantir isso)
        String nomeCompleto = (perfil != null) ? perfil.getNomeCompleto() : null;
        String bio = (perfil != null) ? perfil.getBio() : null;
        LocalDate dataNascimento = (perfil != null) ? perfil.getDataNascimento() : null;

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getPhone_number(),
                nomeCompleto,
                bio,
                dataNascimento
        );
    }
}