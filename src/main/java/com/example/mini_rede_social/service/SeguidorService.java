package com.example.mini_rede_social.service;

import com.example.mini_rede_social.exception.RecursoNaoEncontradoException;
import com.example.mini_rede_social.model.SeguidorModel;
import com.example.mini_rede_social.model.UsuarioModel;
import com.example.mini_rede_social.repository.SeguidorRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class SeguidorService {
    private final SeguidorRepository seguidorRepository;
    private final UsuarioService usuarioService;

    public SeguidorService(SeguidorRepository seguidorRepository, UsuarioService usuarioService) {
        this.seguidorRepository = seguidorRepository;
        this.usuarioService = usuarioService;
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

    public void deixarSeguir(String username) {
        UsuarioModel seguidor = getUsuarioLogado();
        UsuarioModel seguido = usuarioService.buscarPorUsername(username);
        SeguidorModel unfollow = seguidorRepository.findBySeguidorIdAndSeguidoId(seguidor.getId(), seguido.getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Você não está seguindo esse usuário"));

        seguidorRepository.delete(unfollow);
    }
}
