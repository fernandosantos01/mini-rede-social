package com.example.mini_rede_social.repository;

import com.example.mini_rede_social.model.ComentarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ComentarioRepository extends JpaRepository<ComentarioModel, UUID> {
}
