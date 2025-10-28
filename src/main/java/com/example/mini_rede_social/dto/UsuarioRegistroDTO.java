package com.example.mini_rede_social.dto; // Use seu pacote correto

import jakarta.validation.constraints.Email; // Para validar formato de email
import jakarta.validation.constraints.NotBlank; // Garante que não seja nulo nem vazio (após trim)
import jakarta.validation.constraints.NotNull;  // Garante que não seja nulo (redundante com @NotBlank para String, mas ok)
import jakarta.validation.constraints.Size;    // Para tamanho mínimo/máximo

public record UsuarioRegistroDTO(
        @NotNull(message = "Username não pode ser nulo")
        @NotBlank(message = "Username não pode estar em branco")
        String username,

        @NotNull(message = "Senha não pode ser nula")
        @NotBlank(message = "Senha não pode estar em branco")
        @Size(min = 8, message = "Senha deve ter pelo menos 8 caracteres")
        String password,

        @NotNull(message = "Email não pode ser nulo")
        @NotBlank(message = "Email não pode estar em branco")
        @Email(message = "Formato de email inválido")
        String email,

        //(pode adicionar @Pattern se precisar de formato)
        @NotNull(message = "Número de telefone não pode ser nulo")
        @NotBlank(message = "Número de telefone não pode estar em branco")
        String phone_number
) {
}