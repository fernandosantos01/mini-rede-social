package com.example.mini_rede_social.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
public class SupabaseStorageService {

    private final WebClient webClient;
    private final String supabaseUrl;
    private final String supabaseKey;
    private final String bucket;

    public SupabaseStorageService(WebClient.Builder webClientBuilder,
                                  @Value("${supabase.url}") String supabaseUrl,
                                  @Value("${supabase.key}") String supabaseKey,
                                  @Value("${supabase.bucket}") String bucket
        ) {
        this.supabaseUrl = supabaseUrl;
        this.supabaseKey = supabaseKey;
        this.bucket = bucket;

        if (this.supabaseUrl == null || this.supabaseKey == null || this.bucket == null) {
            throw new IllegalStateException("Não foi possível carregar as variáveis do .env! Verifique o arquivo.");
        }
        this.webClient = webClientBuilder.baseUrl(this.supabaseUrl).build();
    }

    public String uploadImage(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo não pode ser vazio.");
        }
        String ext = "";
        String original = file.getOriginalFilename();
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf('.'));
        }
        String path = "images/" + UUID.randomUUID() + ext;

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("file", new ByteArrayResource(file.getBytes()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "form-data; name=\"file\"; filename=\"" + file.getOriginalFilename() + "\"")
                .contentType(MediaType.parseMediaType(Objects.requireNonNull(file.getContentType())));
        // O UPLOAD (POST)
        webClient.post()
                .uri("/storage/v1/object/{bucket}/{path}", this.bucket, path)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.supabaseKey)
                .header("apikey", this.supabaseKey)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return this.supabaseUrl + "/storage/v1/object/public/" + this.bucket + "/" + path;
    }

    public void deletarImagem(String fullPublicUrl) {

        // 1. Validação da URL
        if (fullPublicUrl == null || fullPublicUrl.isBlank()) {
            System.err.println("URL da imagem está nula ou vazia. Pulando deleção no Storage.");
            return;
        }

        // 2. Extrair o "caminho" (key) da URL
        String path;
        try {
            String searchPrefix = this.supabaseUrl + "/storage/v1/object/public/" + this.bucket + "/";

            if (!fullPublicUrl.startsWith(searchPrefix)) {
                throw new IllegalArgumentException("URL não pertence ao bucket configurado: " + fullPublicUrl);
            }

            path = fullPublicUrl.substring(searchPrefix.length());
            path = URLDecoder.decode(path, StandardCharsets.UTF_8);

        } catch (Exception e) {
            System.err.println("Falha ao extrair o caminho da URL do Supabase: " + fullPublicUrl + " | Erro: " + e.getMessage());
            return;
        }

        // 3. ⭐️ A CORREÇÃO (USANDO UriBuilder) ⭐️
        // Executar a chamada DELETE
        try {
            String finalPath = path;
            webClient.delete()
                    // Diz ao WebClient para construir a URI usando o UriBuilder
                    .uri(uriBuilder -> uriBuilder
                            // 1. Define o template do caminho
                            .path("/storage/v1/object/{bucket}/{path}")
                            // 2. Constrói o template, expandindo as variáveis SEM
                            //    codificar as barras (/) que estão DENTRO da var 'path'.
                            .build(this.bucket, finalPath))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.supabaseKey)
                    .header("apikey", this.supabaseKey)// Autenticação
                    .retrieve() // Espera a resposta
                    .bodyToMono(Void.class) // Não esperamos corpo
                    .block(); // Bloqueia até a operação completar

            System.out.println("Arquivo deletado com sucesso do Supabase: " + path);

        } catch (Exception e) {
            System.err.println("Falha ao deletar o arquivo no Supabase Storage: " + path + " | Erro: " + e.getMessage());
            // Em produção, você talvez queira relançar a exceção para
            // o @Transactional do UsuarioService fazer rollback,
            // ou salvar isso num log de "arquivos órfãos".
        }
    }
}