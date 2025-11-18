package com.example.mini_rede_social.repository;

import com.example.mini_rede_social.model.SeguidorModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeguidorRepository extends JpaRepository<SeguidorModel, UUID> {
    @Transactional
    void deleteBySeguidorId(UUID seguidorId);

    @Transactional
    void deleteBySeguidoId(UUID seguidoId);
    Optional<SeguidorModel> findBySeguidorIdAndSeguidoId(UUID seguidorId, UUID seguidoId);

    List<SeguidorModel> findBySeguidorId(UUID seguidorId);

    List<SeguidorModel> findBySeguidoId(UUID seguidoId);
}
