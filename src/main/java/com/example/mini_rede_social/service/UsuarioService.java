package com.example.mini_rede_social.service;

import com.example.mini_rede_social.dto.UsuarioRegistroDTO;
import com.example.mini_rede_social.model.UsuarioModel;
import com.example.mini_rede_social.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public UsuarioModel salvarUsuario(UsuarioRegistroDTO dto) {
        String senhaCriptografada = passwordEncoder.encode(dto.password());
        var usuarioModel = new UsuarioModel();
        BeanUtils.copyProperties(dto, usuarioModel);
        usuarioModel.setPassword(senhaCriptografada);
        return usuarioRepository.save(usuarioModel);
    }

    public List<UsuarioModel> listarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<UsuarioModel> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Optional<UsuarioModel> buscarPorId(UUID id) {
        return usuarioRepository.findById(id);
    }

    @Transactional
    public void deletarUsuario(UsuarioModel usuarioModel) {
        usuarioRepository.delete(usuarioModel);
    }
}
