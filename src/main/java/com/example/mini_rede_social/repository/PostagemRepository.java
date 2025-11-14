package com.example.mini_rede_social.repository;

import com.example.mini_rede_social.model.PostagemModel;
import com.example.mini_rede_social.model.UsuarioModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostagemRepository extends JpaRepository<PostagemModel, UUID> {
    Page<PostagemModel>findByUsuarioIdIn(List<UUID> idsDosAutores, Pageable pageable);

    List<PostagemModel> findByUsuario(UsuarioModel usuarioModel);
}
