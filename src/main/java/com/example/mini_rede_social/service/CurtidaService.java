package com.example.mini_rede_social.service;

import com.example.mini_rede_social.dto.CurtidaResponseDTO;
import com.example.mini_rede_social.exception.RecursoNaoEncontradoException;
import com.example.mini_rede_social.mapper.CurtidaMapper;
import com.example.mini_rede_social.model.CurtidaModel;
import com.example.mini_rede_social.model.PostagemModel;
import com.example.mini_rede_social.model.UsuarioModel;
import com.example.mini_rede_social.repository.CurtidaRepository;
import com.example.mini_rede_social.repository.PostagemRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CurtidaService {
    private final CurtidaRepository curtidaRepository;
    private final PostagemRepository postagemRepository;
    private final UsuarioService usuarioService;
    private final CurtidaMapper curtidaMapper;

    public CurtidaService(CurtidaRepository curtidaRepository, PostagemRepository postagemRepository, UsuarioService usuarioService, CurtidaMapper curtidaMapper) {
        this.curtidaRepository = curtidaRepository;
        this.postagemRepository = postagemRepository;
        this.usuarioService = usuarioService;
        this.curtidaMapper = curtidaMapper;
    }

    private UsuarioModel getUsuarioLogado() {
        String usuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioService.buscarPorUsername(usuarioLogado);
    }

    @Transactional
    public CurtidaResponseDTO curtirPostagem(UUID postagemId) {
        UsuarioModel usuario = getUsuarioLogado();

        PostagemModel postagem = postagemRepository.findById(postagemId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Postagem não encontrada!"));

        CurtidaModel novaCurtida = new CurtidaModel();
        novaCurtida.setUsuario(usuario);
        novaCurtida.setPostagem(postagem);
        return curtidaMapper.toResponseDTO(curtidaRepository.save(novaCurtida));

    }

    @Transactional
    public void descurtirPostagem(UUID postagemId) {
        UsuarioModel usuario = getUsuarioLogado();
        CurtidaModel curtidaParaDeletar = curtidaRepository.findByUsuarioIdAndPostagemId(usuario.getId(), postagemId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Curtida não encontrada. Você não curtiu esta postagem."));
        curtidaRepository.delete(curtidaParaDeletar);
    }

    @Transactional
    public void deletarCurtidasPorPostagemId(UUID postagemId) {
        curtidaRepository.deleteByPostagemId(postagemId);
    }

    @Transactional
    public void deletarCurtidasPorUsuarioId(UUID usuarioId) {
        curtidaRepository.deleteByUsuarioId(usuarioId);
    }

    @Transactional
    public void deletarCurtidasPorPostagemIdEmLote(List<UUID> postIds) {
        curtidaRepository.deleteByPostagemIdIn(postIds);
    }
}
