package com.example.mini_rede_social.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity()
@Table(name = "postagens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostagemModel {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioModel usuario;

    @Column(nullable = false)
    private String conteudoUrl;

    @Column(columnDefinition = "Text")
    private String legenda;

    @Column(updatable = false, nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();
}
