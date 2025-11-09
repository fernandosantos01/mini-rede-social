package com.example.mini_rede_social.repository;

import com.example.mini_rede_social.model.PerfilModel;
import com.example.mini_rede_social.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PerfilRepository extends JpaRepository<PerfilModel, UUID> {
    PerfilModel findByUsuario(UsuarioModel usuario);
}
