package com.example.mini_rede_social.service;

import com.example.mini_rede_social.dto.RegistroCompletoDTO;
import com.example.mini_rede_social.dto.UsuarioResponseDTO;
import com.example.mini_rede_social.exception.RecursoNaoEncontradoException;
import com.example.mini_rede_social.mapper.UsuarioMapper;
import com.example.mini_rede_social.model.PerfilModel;
import com.example.mini_rede_social.model.UsuarioModel;
import com.example.mini_rede_social.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
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

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional
    public UsuarioResponseDTO salvarUsuario(RegistroCompletoDTO dto) {
        String senhaCriptografada = passwordEncoder.encode(dto.password());
        var usuarioModel = new UsuarioModel();
        var perfilModel = new PerfilModel();
        BeanUtils.copyProperties(dto, usuarioModel);
        BeanUtils.copyProperties(dto, perfilModel);
        usuarioModel.setPassword(senhaCriptografada);
        usuarioModel.setPerfil(perfilModel);
        perfilModel.setUsuario(usuarioModel);
        UsuarioModel usuarioSalvo = usuarioRepository.save(usuarioModel);
        return usuarioMapper.toResponseDTO(usuarioSalvo);
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
    public void deletarUsuario(UsuarioModel usuarioModel) {
        usuarioRepository.delete(usuarioModel);
    }
}
