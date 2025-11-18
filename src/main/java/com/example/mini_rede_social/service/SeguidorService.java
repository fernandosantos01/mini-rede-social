package com.example.mini_rede_social.service;

import com.example.mini_rede_social.dto.PerfilResponseDTO;
import com.example.mini_rede_social.exception.RecursoNaoEncontradoException;
import com.example.mini_rede_social.mapper.PerfilMapper;
import com.example.mini_rede_social.model.SeguidorModel;
import com.example.mini_rede_social.model.UsuarioModel;
import com.example.mini_rede_social.repository.SeguidorRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class SeguidorService {
    private final SeguidorRepository seguidorRepository;
    private final UsuarioService usuarioService;
    private final PerfilMapper perfilMapper;
    private final PerfilService perfilService;

    public SeguidorService(SeguidorRepository seguidorRepository, UsuarioService usuarioService, PerfilMapper perfilMapper, PerfilService perfilService) {
        this.seguidorRepository = seguidorRepository;
        this.usuarioService = usuarioService;
        this.perfilMapper = perfilMapper;
        this.perfilService = perfilService;
    }

    private UsuarioModel getUsuarioLogado() {
        String userLogado = SecurityContextHolder.getContext().getAuthentication().getName();

        return usuarioService.buscarPorUsername(userLogado);
    }

    public void seguir(String username) {
        UsuarioModel seguidor = getUsuarioLogado();
        UsuarioModel seguido = usuarioService.buscarPorUsername(username);
        if (seguido.getId().equals(seguidor.getId())) {
            throw new IllegalArgumentException("Você não pode seguir a si mesmo.");
        }
        SeguidorModel seguidorModel = new SeguidorModel();
        seguidorModel.setSeguidor(seguidor);
        seguidorModel.setSeguido(seguido);
        seguidorRepository.save(seguidorModel);
    }

    @Transactional
    public void deixarSeguir(String username) {
        UsuarioModel seguidor = getUsuarioLogado();
        UsuarioModel seguido = usuarioService.buscarPorUsername(username);
        SeguidorModel unfollow = seguidorRepository.findBySeguidorIdAndSeguidoId(seguidor.getId(), seguido.getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Você não está seguindo esse usuário"));
        seguidorRepository.delete(unfollow);
    }

    public List<PerfilResponseDTO> listarSeguindo(String username) {
        UsuarioModel alvo = usuarioService.buscarPorUsername(username);
        List<SeguidorModel> relacoes = seguidorRepository.findBySeguidorId(alvo.getId());
        List<UUID> idsDosSeguidos = relacoes.stream()
                .map(relacao -> relacao.getSeguido().getId())
                .collect(Collectors.toList());
        return perfilService.buscarPerfisPorUsuarioIds(idsDosSeguidos);
    }

    public List<PerfilResponseDTO> listarSeguidores(String username) {
        UsuarioModel alvo = usuarioService.buscarPorUsername(username);
        List<SeguidorModel> relacoes = seguidorRepository.findBySeguidoId(alvo.getId());
        List<UUID> idsDosSeguidores = relacoes.stream()
                .map(relacao -> relacao.getSeguidor().getId())
                .collect(Collectors.toList());
        return perfilService.buscarPerfisPorUsuarioIds(idsDosSeguidores);
    }

    public List<UUID> listarIdsSeguindo(UUID seguidorId) {
        List<SeguidorModel> relacoes = seguidorRepository.findBySeguidoId(seguidorId);

        return relacoes.stream()
                .map(relacao -> relacao.getSeguido().getId())
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletarTodasRelacoesDoUsuario(UUID usuarioId) {
        seguidorRepository.deleteBySeguidorId(usuarioId);
        seguidorRepository.deleteBySeguidoId(usuarioId);
    }
}
