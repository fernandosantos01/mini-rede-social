package com.example.mini_rede_social.service;

import com.example.mini_rede_social.dto.AutorDTO;
import com.example.mini_rede_social.dto.PostagemAtualizacaoDTO;
import com.example.mini_rede_social.dto.PostagemCriacaoDTO;
import com.example.mini_rede_social.dto.PostagemResponseDTO;
import com.example.mini_rede_social.exception.RecursoNaoEncontradoException;
import com.example.mini_rede_social.mapper.PostagemMapper;
import com.example.mini_rede_social.model.PostagemModel;
import com.example.mini_rede_social.model.SeguidorModel;
import com.example.mini_rede_social.model.UsuarioModel;
import com.example.mini_rede_social.repository.PostagemRepository;
import com.example.mini_rede_social.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final PostagemMapper postagemMapper;
    private final SupabaseStorageService supabaseStorageService;
    private final SeguidorService seguidorService;
    private final ComentarioService comentarioService;
    private final CurtidaService curtidaService;

    public PostagemService(PostagemRepository postagemRepository, UsuarioRepository usuarioRepository, SupabaseStorageService supabaseStorageService, PostagemMapper postagemMapper, @Lazy SeguidorService seguidorService, @Lazy ComentarioService comentarioService, @Lazy CurtidaService curtidaService) {
        this.postagemRepository = postagemRepository;
        this.usuarioRepository = usuarioRepository;
        this.supabaseStorageService = supabaseStorageService;
        this.postagemMapper = postagemMapper;
        this.seguidorService = seguidorService;
        this.comentarioService = comentarioService;
        this.curtidaService = curtidaService;
    }

    @Transactional
    public PostagemResponseDTO criarPostagem(PostagemCriacaoDTO postagemCriacaoDTO) throws IOException {
        String usernameLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        UsuarioModel autor = usuarioRepository.findByUsername(usernameLogado)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário Não Encontrado" + usernameLogado));

        String imageUrl = supabaseStorageService.uploadImage(postagemCriacaoDTO.imagem());

        PostagemModel novaPostagem = new PostagemModel();
        novaPostagem.setUsuario(autor);
        novaPostagem.setConteudoUrl(imageUrl);
        novaPostagem.setLegenda(postagemCriacaoDTO.legenda());

        PostagemModel postagemSalva = postagemRepository.save(novaPostagem);
        return postagemMapper.toResponseDTO(postagemSalva);
    }

    public List<PostagemResponseDTO> buscarTodas() {
        List<PostagemModel> postagens = postagemRepository.findAll();

        return postagens.stream()
                .map(postagemMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public PostagemResponseDTO buscarPorId(UUID id) {
        var postagemModel = postagemRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Postagem com ID " + id + "não encontrada."));
        return postagemMapper.toResponseDTO(postagemModel);

    }

    public PostagemModel buscarEntidadePorId(UUID id) {
        return postagemRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Postagem com ID " + id + " não encontrada."));
    }

    @Transactional
    public PostagemResponseDTO atualizarPostagem(UUID id, PostagemAtualizacaoDTO dto) throws IOException {
        PostagemModel postagemExistente = verificarPermissaoEBusca(id);

        String urlAntiga = postagemExistente.getConteudoUrl();

        if (dto.imagem() != null && !dto.imagem().isEmpty()) {
            String novaUrl = supabaseStorageService.uploadImage(dto.imagem());
            postagemExistente.setConteudoUrl(novaUrl);
        }

        if (dto.legenda() != null) {
            postagemExistente.setLegenda(dto.legenda());
        }

        PostagemModel postagemSalva = postagemRepository.save(postagemExistente);

        if (dto.imagem() != null && !dto.imagem().isEmpty() && urlAntiga != null) {
            try {
                supabaseStorageService.deletarImagem(urlAntiga);
            } catch (Exception ex) {
                System.err.println("Falha ao deletar a imagem antiga do Supabase " + urlAntiga);
            }
        }
        return postagemMapper.toResponseDTO(postagemSalva);
    }

    @Transactional
    public void deletarPostagem(UUID id) {
        PostagemModel postagemParaDeletar = verificarPermissaoEBusca(id);

        comentarioService.deletarComentariosDaPostagem(id);

        curtidaService.deletarCurtidasPorPostagemId(id);


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

    public Page<PostagemModel> buscarPostagensPorListaDeAutores(List<UUID> idsDosAutores, Pageable pageable) {
        return postagemRepository.findByUsuarioIdIn(idsDosAutores, pageable);
    }

    @Transactional
    public void deletarTodasAsPostagensDoUsuario(UUID usuarioId) {

        List<PostagemModel> postagens = postagemRepository.findByUsuarioId(usuarioId);

        if (postagens.isEmpty()) {
            return;
        }
        List<UUID> postIds = postagens.stream()
                .map(PostagemModel::getId)
                .collect(Collectors.toList());
        List<String> urlsImagens = postagens.stream()
                .map(PostagemModel::getConteudoUrl)
                .toList();

        comentarioService.deletarComentariosPorPostagemIdEmLote(postIds);
        curtidaService.deletarCurtidasPorPostagemIdEmLote(postIds);
        postagemRepository.deleteAllInBatch(postagens);

        //DELEÇÃO DAS IMAGENS (A parte "lenta" que é inevitável)
        // Isso é feito DEPOIS que o banco já foi limpo (a transação pode fechar)
        // Idealmente, isso seria assíncrono (@Async), mas por enquanto:
        for (String url : urlsImagens) {
            try {
                supabaseStorageService.deletarImagem(url);
            } catch (Exception e) {
                System.err.println("Falha ao deletar imagem órfã do Supabase: " + url);
            }
        }
    }
}
