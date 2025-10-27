package com.example.mini_rede_social.repository;

import com.example.mini_rede_social.model.SeguidorModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SeguidorRepository extends JpaRepository<SeguidorModel, UUID> {
}
