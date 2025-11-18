package com.example.mini_rede_social.repository;

import com.example.mini_rede_social.model.ComentarioModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ComentarioRepository extends JpaRepository<ComentarioModel, UUID> {
    @Transactional
    void deleteByPostagemId(UUID postagemId);

    @Transactional
    void deleteByUsuarioId(UUID usuarioId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ComentarioModel c WHERE c.postagem.id IN :postIds")
    void deleteByPostagemIdIn(@Param("postIds") List<UUID> postIds);

    List<ComentarioModel> findByPostagemId(UUID postagemId);

    List<ComentarioModel> findByPostagemIdOrderByDataCriacaoDesc(UUID postagemId);
}
