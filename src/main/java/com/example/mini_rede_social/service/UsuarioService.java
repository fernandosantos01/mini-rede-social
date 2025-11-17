package com.example.mini_rede_social.service;

import com.example.mini_rede_social.dto.RegistroCompletoDTO;
import com.example.mini_rede_social.dto.UsuarioResponseDTO;
import com.example.mini_rede_social.exception.RecursoNaoEncontradoException;
import com.example.mini_rede_social.mapper.UsuarioMapper;
import com.example.mini_rede_social.model.PerfilModel;
import com.example.mini_rede_social.model.UsuarioModel;
import com.example.mini_rede_social.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;
    private final SeguidorService seguidorService;
    private final CurtidaService curtidaService;
    private final ComentarioService comentarioService;
    private final PostagemService postagemService;
    private final PerfilService perfilService;

    @PersistenceContext
    private EntityManager entityManager;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, @Lazy SeguidorService seguidorService, @Lazy CurtidaService curtidaService, @Lazy ComentarioService comentarioService, @Lazy PostagemService postagemService, @Lazy PerfilService perfilService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.usuarioMapper = usuarioMapper;
        this.seguidorService = seguidorService;
        this.curtidaService = curtidaService;
        this.comentarioService = comentarioService;
        this.postagemService = postagemService;
        this.perfilService = perfilService;
    }

    @Transactional
    public UsuarioResponseDTO salvarUsuario(RegistroCompletoDTO dto) {
        String senhaCriptografada = passwordEncoder.encode(dto.password());

        UsuarioModel usuarioModel = usuarioMapper.toUsuarioModel(dto);
        usuarioModel.setPassword(senhaCriptografada);
        UsuarioModel usuarioSalvo = usuarioRepository.save(usuarioModel);

        PerfilModel perfilModel = usuarioMapper.toPerfilModel(dto);
        perfilModel.setUsuario(usuarioSalvo);

        perfilService.salvarPerfil(perfilModel);

        return usuarioMapper.toResponseDTO(usuarioSalvo, perfilModel);
    }

    public List<UsuarioModel> listarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public UsuarioModel buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Username " + username + " não encontrado"));

    }

    public UsuarioModel buscarPorId(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário com " + id + " não encontrado"));
    }

    @Transactional
    public void deletarMinhaContaLogada() {
        UsuarioModel usuarioLogado = getUsuarioLogado();
        UUID usuarioId = usuarioLogado.getId();

        seguidorService.deletarTodasRelacoesDoUsuario(usuarioId);
        curtidaService.deletarCurtidasPorUsuarioId(usuarioId);
        comentarioService.deletarUsuarioDaPostagem(usuarioId);
        postagemService.deletarTodasAsPostagensDoUsuario(usuarioId);
        perfilService.deletarPerfilPorUsuarioId(usuarioId);
        entityManager.flush();
        entityManager.clear();

        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado para deleção final.");
        }
        usuarioRepository.deleteById(usuarioId);
    }

    private UsuarioModel getUsuarioLogado() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return buscarPorUsername(username);
    }
}
