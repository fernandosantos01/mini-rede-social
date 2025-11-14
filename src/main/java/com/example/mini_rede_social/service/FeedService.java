package com.example.mini_rede_social.service;

import com.example.mini_rede_social.dto.PostagemResponseDTO;
import com.example.mini_rede_social.mapper.PostagemMapper;
import com.example.mini_rede_social.model.PostagemModel;
import com.example.mini_rede_social.model.UsuarioModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FeedService {
    private final PostagemService postagemService;
    private final UsuarioService usuarioService;
    private final SeguidorService seguidorService;
    private final PostagemMapper postagemMapper;

    public FeedService(PostagemService postagemService, UsuarioService usuarioService, SeguidorService seguidorService, PostagemMapper postagemMapper) {
        this.postagemService = postagemService;
        this.usuarioService = usuarioService;
        this.seguidorService = seguidorService;
        this.postagemMapper = postagemMapper;
    }

    private UsuarioModel getUsuarioLogado() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return usuarioService.buscarPorUsername(username);
    }

    public Page<PostagemResponseDTO> buscarFeed(Pageable pageable) {
        UsuarioModel usuarioLogado = getUsuarioLogado();

        List<UUID> idsDosSeguidos = seguidorService.listarIdsSeguindo(usuarioLogado.getId());

        idsDosSeguidos.add(usuarioLogado.getId());

        Page<PostagemModel> paginaDePostagens = postagemService.buscarPostagensPorListaDeAutores(idsDosSeguidos, pageable);
        return paginaDePostagens.map(postagemMapper::toResponseDTO);
    }
}
