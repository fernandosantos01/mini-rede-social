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

    public SupabaseStorageService(
            WebClient.Builder webClientBuilder,
            @Value("${supabase.url}") String supabaseUrl,
            @Value("${supabase.key}") String supabaseKey,
            @Value("${supabase.bucket}") String bucket
    ) {
        this.webClient = webClientBuilder.baseUrl(supabaseUrl).build();
        this.supabaseUrl = supabaseUrl;
        this.supabaseKey = supabaseKey;
        this.bucket = bucket;
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
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return this.supabaseUrl + "/storage/v1/object/public/" + this.bucket + "/" + path;
    }

    public void deletarImagem(String fullPublicUrl) {
        if (fullPublicUrl == null || fullPublicUrl.isBlank()) {
            System.err.println("URL da imagem está nula ou vazia. Pulando deleção no Storage.");
            return;
        }
        String path;
        try {
            // Monta o prefixo da URL pública para sabermos o que remover
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
        try {
            webClient.delete()
                    .uri("/storage/v1/object/{bucket}/{path}", this.bucket, path)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.supabaseKey)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

            System.out.println("Arquivo deletado com sucesso do Supabase: " + path);

        } catch (Exception e) {
            System.err.println("Falha ao deletar o arquivo no Supabase Storage: " + path + " | Erro: " + e.getMessage());
        }
    }
}