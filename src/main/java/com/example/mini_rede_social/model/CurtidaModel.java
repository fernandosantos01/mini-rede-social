package com.example.mini_rede_social.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "curtidas", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"usuario_id", "postagem_id"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurtidaModel {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioModel usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postagem_id", nullable = false)
    private PostagemModel postagem;
}
