package com.example.mini_rede_social.repository;

import com.example.mini_rede_social.model.PostagemModel;
import com.example.mini_rede_social.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostagemRepository extends JpaRepository<PostagemModel, UUID> {
    List<PostagemModel> findByUsuario(UsuarioModel usuarioModel);
}
