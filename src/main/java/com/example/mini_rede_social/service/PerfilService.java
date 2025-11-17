package com.example.mini_rede_social.service;

import com.example.mini_rede_social.dto.PerfilAtualizacaoDTO;
import com.example.mini_rede_social.dto.PerfilResponseDTO;
import com.example.mini_rede_social.exception.RecursoNaoEncontradoException;
import com.example.mini_rede_social.mapper.PerfilMapper;
import com.example.mini_rede_social.model.PerfilModel;
import com.example.mini_rede_social.model.UsuarioModel;
import com.example.mini_rede_social.repository.PerfilRepository;
import com.example.mini_rede_social.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PerfilService {
    private final PerfilRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilMapper perfilMapper;

    public PerfilService(PerfilRepository perfilRepository, UsuarioRepository usuarioRepository, PerfilMapper perfilMapper) {
        this.perfilRepository = perfilRepository;
        this.usuarioRepository = usuarioRepository;
        this.perfilMapper = perfilMapper;
    }

    private PerfilModel getPerfilDoUsuarioLogado() {
        String usernameLogado = SecurityContextHolder.getContext().getAuthentication().getName();

        UsuarioModel usuario = usuarioRepository.findByUsername(usernameLogado)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário Não Encontrado " + usernameLogado));

        return perfilRepository.findById(usuario.getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Perfil não encontrado " + usernameLogado));
    }

    public PerfilResponseDTO buscarPerfilPorUsername(String username) {
        UsuarioModel usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário com username " + username + " não encontrado"));

        return converterPerfilParaDTO(perfilRepository.findById(usuario.getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Perfil não encontrado " + username)));
    }

    public void salvarPerfil(PerfilModel perfil) {
        perfilRepository.save(perfil);
    }

    @Transactional
    public PerfilResponseDTO atualizarPerfil(PerfilAtualizacaoDTO dto) {
        PerfilModel perfil = getPerfilDoUsuarioLogado();

        if (dto.nomeCompleto() != null && !dto.nomeCompleto().isBlank()) {
            perfil.setNomeCompleto(dto.nomeCompleto());
        }
        if (dto.bio() != null) {
            perfil.setBio(dto.bio());
        }
        if (dto.dataNascimento() != null) {
            perfil.setDataNascimento(dto.dataNascimento());
        }
        return converterPerfilParaDTO(perfilRepository.save(perfil));
    }

    private PerfilResponseDTO converterPerfilParaDTO(PerfilModel perfil) {
        return new PerfilResponseDTO(
                perfil.getId(),
                perfil.getNomeCompleto(),
                perfil.getBio(),
                perfil.getDataNascimento()
        );
    }

    @Transactional
    public void deletarPerfilPorUsuarioId(UUID usuarioId) {
        if (perfilRepository.existsById(usuarioId)) {
            perfilRepository.deleteById(usuarioId);
        }
    }

    public List<PerfilResponseDTO> buscarPerfisPorUsuarioIds(List<UUID> usuarioIds) {
        List<PerfilModel> perfis = perfilRepository.findAllById(usuarioIds);
        return perfis.stream()
                .map(perfilMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public PerfilModel buscarPerfilPorUsuarioId(UUID usuarioId) {
        return perfilRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Perfil não encontrado para o usuário: " + usuarioId));
    }
}
