package com.example.mini_rede_social.dto;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PerfilAtualizacaoDTO(@Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
                                   String nomeCompleto,

                                   @Size(max = 255, message = "Bio não pode exceder 255 caracteres")
                                   String bio,

                                   @Past(message = "Data de Nascimento deve estar no passado")
                                   LocalDate dataNascimento
) {
}
