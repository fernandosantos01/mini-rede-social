package com.example.mini_rede_social.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "perfis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilModel {
    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "usuario_id")
    private UsuarioModel usuario;

    @Column(nullable = false, length = 100)
    private String nomeCompleto;
    @Column()
    private String bio;
    @Column(nullable = false)
    private LocalDate dataNascimento;
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Transient
    public Integer getIdade() {
        if (this.dataNascimento == null) {
            return null;
        }
        return java.time.Period.between(this.dataNascimento, LocalDate.now()).getYears();
    }
}
