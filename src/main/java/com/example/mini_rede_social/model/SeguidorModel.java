package com.example.mini_rede_social.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "seguidores", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"seguidor_id", "seguido_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SeguidorModel {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seguidor_id", nullable = false)
    private UsuarioModel seguidor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seguido_id", nullable = false)
    private UsuarioModel seguido;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataSeguindo = LocalDateTime.now();
}
