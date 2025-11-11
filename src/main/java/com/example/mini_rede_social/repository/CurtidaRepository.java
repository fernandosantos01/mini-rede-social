package com.example.mini_rede_social.repository;

import com.example.mini_rede_social.model.CurtidaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CurtidaRepository extends JpaRepository<CurtidaModel, UUID> {
    Optional<CurtidaModel> findByUsuarioIdAndPostagemId(UUID usuarioId, UUID postagemId);
}
