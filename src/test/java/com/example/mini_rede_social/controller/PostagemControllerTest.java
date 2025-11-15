package com.example.mini_rede_social.controller;

import com.example.mini_rede_social.model.PostagemModel;
import com.example.mini_rede_social.model.UsuarioModel;
import com.example.mini_rede_social.repository.PostagemRepository;
import com.example.mini_rede_social.repository.UsuarioRepository;
import com.example.mini_rede_social.service.SupabaseStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile; // Importante
import org.springframework.security.test.context.support.WithMockUser; // Importante
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*; // Importar multipart
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*; // Importar status e jsonPath

@SpringBootTest // 1. Sobe a API inteira
@AutoConfigureMockMvc // 2. Nos dá o "Postman Automatizado" (MockMvc)
@ActiveProfiles("test") // 3. Força o uso do application-test.properties (H2)
class PostagemControllerTest {

    @Autowired
    private MockMvc mockMvc; // O "Postman"

    // 4. MOCK: Substitui o SupabaseService real por um "dublê" (mock)
    @MockBean
    private SupabaseStorageService supabaseStorageService;

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PostagemRepository postagemRepository;

    // Variáveis de setup
    private UsuarioModel usuarioDono;
    private UsuarioModel usuarioIntruso;

    @BeforeEach
    void setup() {
        // Limpa o banco H2 antes de cada teste
        postagemRepository.deleteAll();
        usuarioRepository.deleteAll();

        // Cria os usuários de teste
        // (Nota: No Spring Security real, o UserDetailsService precisa disso)
        usuarioDono = new UsuarioModel();
        usuarioDono.setUsername("dono");
        usuarioDono.setPassword("senhaCriptografada"); // O mock ignora a senha
        // ... (setar outros campos obrigatórios: email, phone)
        usuarioRepository.save(usuarioDono);

        usuarioIntruso = new UsuarioModel();
        usuarioIntruso.setUsername("intruso");
        usuarioIntruso.setPassword("senhaCriptografada");
        // ... (setar outros campos obrigatórios)
        usuarioRepository.save(usuarioIntruso);
    }


    // TESTE 1: A ROTA PÚBLICA (Health Check)
    @Test
    void getStatus_deveRetornarOk_quandoPublico() throws Exception {
        mockMvc.perform(get("/api/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("API no ar!"));
    }


    // TESTE 2: A ROTA PROTEGIDA (POSTAGEM)
    @Test
    @WithMockUser(username = "dono") // 5. FINGE QUE O "dono" ESTÁ LOGADO!
    void criarPostagem_quandoAutenticado_deveRetornarCreated() throws Exception {

        // 6. Prepara o Mock do Supabase
        // "Quando o 'uploadImage' for chamado com QUALQUER arquivo, retorne esta URL"
        when(supabaseStorageService.uploadImage(any(MultipartFile.class)))
                .thenReturn("https://url.fake.do.supabase/imagem.jpg");

        // 7. Prepara o arquivo "falso" do upload
        MockMultipartFile imagem = new MockMultipartFile(
                "imagem", // O nome do @RequestParam (Key do Postman)
                "imagem.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "conteudo da imagem".getBytes()
        );

        MockMultipartFile legenda = new MockMultipartFile(
                "legenda",
                "",
                MediaType.TEXT_PLAIN_VALUE,
                "Minha primeira postagem".getBytes()
        );

        // 8. Executa a requisição 'multipart/form-data'
        mockMvc.perform(multipart("/api/postagens")
                                .file(imagem)
                                .file(legenda)
                        // Não precisa do .header("Authorization"), o @WithMockUser já faz isso
                )
                .andExpect(status().isCreated()) // Espera 201
                .andExpect(jsonPath("$.legenda").value("Minha primeira postagem"))
                .andExpect(jsonPath("$.conteudoUrl").value("https://url.fake.do.supabase/imagem.jpg"));
    }


    // TESTE 3: A REGRA DE SEGURANÇA (PERMISSÃO)
    @Test
    @WithMockUser(username = "intruso") // 9. O "INTRUSO" ESTÁ LOGADO
    void deletarPostagem_quandoUsuarioNaoEhDono_deveRetornar403Forbidden() throws Exception {

        // Cria um post como o "dono"
        PostagemModel postDoDono = new PostagemModel();
        postDoDono.setUsuario(usuarioDono);
        postDoDono.setConteudoUrl("http://...");
        postDoDono = postagemRepository.save(postDoDono);

        // 10. O "intruso" tenta deletar o post do "dono"
        mockMvc.perform(delete("/api/postagens/" + postDoDono.getId()))
                .andExpect(status().isForbidden()); // Espera 403 (O Handler Global deve pegar a SecurityException)
    }
}