package com.example.mini_rede_social.service;

import com.example.mini_rede_social.dto.ComentarioCriacaoDTO;
import com.example.mini_rede_social.dto.ComentarioResponseDTO;
import com.example.mini_rede_social.exception.RecursoNaoEncontradoException;
import com.example.mini_rede_social.mapper.ComentarioMapper;
import com.example.mini_rede_social.model.ComentarioModel;
import com.example.mini_rede_social.model.PostagemModel;
import com.example.mini_rede_social.model.UsuarioModel;
import com.example.mini_rede_social.repository.ComentarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ComentarioService {
    private final UsuarioService usuarioService;
    private final ComentarioRepository comentarioRepository;
    private final PostagemService postagemService;
    private final ComentarioMapper comentarioMapper;

    public ComentarioService(UsuarioService usuarioService, ComentarioRepository comentarioRepository, PostagemService postagemService, ComentarioMapper comentarioMapper) {
        this.usuarioService = usuarioService;
        this.comentarioRepository = comentarioRepository;
        this.postagemService = postagemService;
        this.comentarioMapper = comentarioMapper;
    }

    private UsuarioModel getUsuarioLogado() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return usuarioService.buscarPorUsername(username);
    }

    @Transactional
    public ComentarioResponseDTO criarComentario(UUID postagemId, ComentarioCriacaoDTO dtoComentario) {
        UsuarioModel autor = getUsuarioLogado();
        PostagemModel postagem = postagemService.buscarEntidadePorId(postagemId);

        ComentarioModel novoComentario = new ComentarioModel();
        novoComentario.setPostagem(postagem);
        novoComentario.setUsuario(autor);
        novoComentario.setTexto(dtoComentario.texto());

        ComentarioModel salvo = comentarioRepository.save(novoComentario);

        return comentarioMapper.toResponseDTO(salvo);
    }

    public List<ComentarioResponseDTO> listarComentariosDaPostagem(UUID postagemId) {
        postagemService.buscarEntidadePorId(postagemId);

        return comentarioRepository.findByPostagemId(postagemId)
                .stream()
                .map(comentarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletarComentario(UUID comentarioId) {
        ComentarioModel comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Comentário não encontrado!!"));
        UsuarioModel usuarioLogado = getUsuarioLogado();

        boolean ehDonoDoComentario = comentario.getUsuario().getId().equals(usuarioLogado.getId());
        boolean ehDonoDaPostagem = comentario.getPostagem().getUsuario().getId().equals(usuarioLogado.getId());

        if (!ehDonoDaPostagem && !ehDonoDoComentario) {
            throw new SecurityException("Você não tem permissão para deletar este comentário.");
        }
        comentarioRepository.delete(comentario);

    }
}
