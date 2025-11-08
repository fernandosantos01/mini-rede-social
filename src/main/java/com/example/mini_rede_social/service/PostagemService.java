package com.example.mini_rede_social.service;

import com.example.mini_rede_social.dto.AutorDTO;
import com.example.mini_rede_social.dto.PostagemCriacaoAtualizacaoDTO;
import com.example.mini_rede_social.dto.PostagemResponseDTO;
import com.example.mini_rede_social.exception.RecursoNaoEncontradoException;
import com.example.mini_rede_social.model.PostagemModel;
import com.example.mini_rede_social.model.UsuarioModel;
import com.example.mini_rede_social.repository.PostagemRepository;
import com.example.mini_rede_social.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostagemService {
    private final PostagemRepository postagemRepository;
    private final UsuarioRepository usuarioRepository;
    private final SupabaseStorageService supabaseStorageService;

    public PostagemService(PostagemRepository postagemRepository, UsuarioRepository usuarioRepository, SupabaseStorageService supabaseStorageService) {
        this.postagemRepository = postagemRepository;
        this.usuarioRepository = usuarioRepository;
        this.supabaseStorageService = supabaseStorageService;
    }

    @Transactional
    public PostagemResponseDTO criarPostagem(PostagemCriacaoAtualizacaoDTO postagemCriacaoAtualizacaoDTO) throws IOException {
        String usernameLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        UsuarioModel autor = usuarioRepository.findByUsername(usernameLogado)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário Não Encontrado" + usernameLogado));

        String imageUrl = supabaseStorageService.uploadImage(postagemCriacaoAtualizacaoDTO.imagem());

        PostagemModel novaPostagem = new PostagemModel();
        novaPostagem.setUsuario(autor);
        novaPostagem.setConteudoUrl(imageUrl);
        novaPostagem.setLegenda(postagemCriacaoAtualizacaoDTO.legenda());

        PostagemModel postagemSalva = postagemRepository.save(novaPostagem);
        return converterParaDTO(postagemSalva);
    }

    public List<PostagemResponseDTO> buscarTodas() {
        List<PostagemModel> postagens = postagemRepository.findAll();

        return postagens.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public PostagemResponseDTO buscarPorId(UUID id) {
        var postagemModel = postagemRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Postagem com ID " + id + "não encontrada."));
        return converterParaDTO(postagemModel);

    }
    private PostagemModel buscarEntidadePorId(UUID id) {
        return postagemRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Postagem com ID " + id + " não encontrada."));
    }

    @Transactional
    public PostagemModel atualizarPostagem(UUID id, PostagemCriacaoAtualizacaoDTO dto) {
        PostagemModel postagemExistente = verificarPermissaoEBusca(id);

        postagemExistente.setLegenda(dto.legenda());

        return postagemRepository.save(postagemExistente);
    }

    @Transactional
    public void deletarPostagem(UUID id) {
        PostagemModel postagemParaDeletar = verificarPermissaoEBusca(id);

        supabaseStorageService.deletarImagem(postagemParaDeletar.getConteudoUrl());

        postagemRepository.delete(postagemParaDeletar);
    }


    private PostagemModel verificarPermissaoEBusca(UUID postagemId) {
        String usernameLogado = SecurityContextHolder.getContext().getAuthentication().getName();

        PostagemModel postagem = buscarEntidadePorId(postagemId);

        if (!postagem.getUsuario().getUsername().equals(usernameLogado)) {
            throw new SecurityException("Acesso negado: Usuário não é o autor da postagem.");
        }
        return postagem;
    }

    public PostagemResponseDTO converterParaDTO(PostagemModel postagemModel) {
        AutorDTO autorDTO = new AutorDTO(
                postagemModel.getUsuario().getId(),
                postagemModel.getUsuario().getUsername());

        return new PostagemResponseDTO(
                postagemModel.getId(),
                postagemModel.getConteudoUrl(),
                postagemModel.getLegenda(),
                postagemModel.getDataCriacao(),
                autorDTO
        );
    }
}
