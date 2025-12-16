package com.example.mini_rede_social.service;

import com.example.mini_rede_social.dto.RegistroCompletoDTO;
import com.example.mini_rede_social.exception.RecursoNaoEncontradoException;
import com.example.mini_rede_social.mapper.UsuarioMapper;
import com.example.mini_rede_social.model.PerfilModel;
import com.example.mini_rede_social.model.UsuarioModel;
import com.example.mini_rede_social.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UsuarioMapper usuarioMapper;
    @Mock
    private PerfilService perfilService;

    @InjectMocks
    private UsuarioService usuarioService;

    private UUID usuarioId;
    private UsuarioModel usuarioModel;
    private RegistroCompletoDTO registroCompletoDTO;

    @BeforeEach
    void setup() {
        usuarioId = UUID.randomUUID();
        usuarioModel = new UsuarioModel();
        usuarioModel.setId(usuarioId);
        usuarioModel.setUsername("teste_dev");
        usuarioModel.setPassword("senha_criptografada");

        registroCompletoDTO = new RegistroCompletoDTO(
                "novo_dev",
                "novo@email.com",
                "9999",
                "senha_criptografada",
                "Novo Desenvolvedor",
                LocalDate.now(),
                "Bio"
        );
    }

    @Test
    void testSalvarUsuario_deveCriptografarSenhaESalvarUsuario() {
        String HASH_ESPERADO = "hash_falso_para_teste";
        when(passwordEncoder.encode(registroCompletoDTO.password())).thenReturn(HASH_ESPERADO);
        when(usuarioMapper.toUsuarioModel(any(RegistroCompletoDTO.class))).thenReturn(usuarioModel);
        when(perfilService.salvarPerfil(any(PerfilModel.class))).thenReturn(any(PerfilModel.class));

        perfilService.salvarPerfil(usuarioMapper.toPerfilModel(registroCompletoDTO));
        usuarioService.salvarUsuario(registroCompletoDTO);

        verify(usuarioRepository, times(1)).save(usuarioModel);
        verify(passwordEncoder, times(1)).encode(registroCompletoDTO.password());
    }

    @Test
    void buscarPorUsername_deveRetornarUsuario_quandoEncontrado() {
        // ARRANGE
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuarioModel));

        // ACT
        UsuarioModel resultado = usuarioService.buscarPorUsername("teste_dev");

        // ASSERT
        // Verifica se o resultado não é nulo e se o objeto retornado é o esperado
        assertNotNull(resultado);
        assertEquals("teste_dev", resultado.getUsername());
    }

    @Test
    void buscarPorUsername_deveLancarExcecao_quandoNaoEncontrado() {
        // ARRANGE
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // ACT & ASSERT
        // Verifica se a exceção customizada (RecursoNaoEncontradoException) é lançada
        assertThrows(RecursoNaoEncontradoException.class, () -> {
            usuarioService.buscarPorUsername("nao_existe");
        });
    }
}
