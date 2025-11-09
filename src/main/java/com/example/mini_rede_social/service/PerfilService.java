package com.example.mini_rede_social.service;

import com.example.mini_rede_social.dto.PerfilAtualizacaoDTO;
import com.example.mini_rede_social.exception.RecursoNaoEncontradoException;
import com.example.mini_rede_social.model.PerfilModel;
import com.example.mini_rede_social.model.UsuarioModel;
import com.example.mini_rede_social.repository.PerfilRepository;
import com.example.mini_rede_social.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PerfilService {
    private final PerfilRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;

    public PerfilService(PerfilRepository perfilRepository, UsuarioRepository usuarioRepository) {
        this.perfilRepository = perfilRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private PerfilModel getPerfilDoUsuarioLogado() {
        String usernameLogado = SecurityContextHolder.getContext().getAuthentication().getName();

        UsuarioModel usuario = usuarioRepository.findByUsername(usernameLogado)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário Não Encontrado " + usernameLogado));

        return perfilRepository.findById(usuario.getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Perfil não encontrado " + usernameLogado));
    }

    public PerfilModel buscarPerfilPorUsername(String username) {
        UsuarioModel usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário com username " + username + " não encontrado"));

        return perfilRepository.findById(usuario.getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Perfil não encontrado " + username));
    }

    @Transactional
    public PerfilModel atualizarPerfil(PerfilAtualizacaoDTO dto) {
        PerfilModel perfilParaAtualizar = getPerfilDoUsuarioLogado();

        if (dto.nomeCompleto() != null && !dto.nomeCompleto().isBlank()) {
            perfilParaAtualizar.setNomeCompleto(dto.nomeCompleto());
        }
        if (dto.bio() != null) {
            perfilParaAtualizar.setBio(dto.bio());
        }
        if (dto.dataNascimento() != null) {
            perfilParaAtualizar.setDataNascimento(dto.dataNascimento());
        }

        return perfilRepository.save(perfilParaAtualizar);
    }
}
