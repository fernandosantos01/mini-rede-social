package com.example.mini_rede_social.repository;

import com.example.mini_rede_social.model.ComentarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ComentarioRepository extends JpaRepository<ComentarioModel, UUID> {

    List<ComentarioModel> findByPostagemId(UUID postagemId);

    List<ComentarioModel> findByPostagemIdOrderByDataCriacaoDesc(UUID postagemId);
}
