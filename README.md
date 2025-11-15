# 🧭 Mini Rede Social

> Projeto desenvolvido por **José Fernando**, acadêmico de Ciência da Computação (UFPI) e desenvolvedor Back-End focado em Java com Spring Boot.

---

## 📘 Visão Geral

A **Mini Rede Social** é uma API RESTful completa que simula o funcionamento de uma rede social moderna. O projeto foi desenvolvido com foco em **arquitetura limpa (SOLID)**, **padrões de design (Pattern)** e **segurança ponta-a-ponta**.

A aplicação permite:

-   Criação de perfis de usuário (com dados de autenticação e dados públicos separados).
-   Upload de postagens com mídia (fotos) direto para a nuvem (Supabase Storage).
-   Sistema de Curtidas, Comentários, Seguir e Deixar de Seguir.
-   Geração de um Feed principal complexo, paginado e ordenado.
-   Autenticação e autorização via JWT (Stateless).

---

## ⚙️ Tecnologias Utilizadas

| Categoria | Tecnologias |
| :--- | :--- |
| Linguagem | **Java 21** |
| Framework | **Spring Boot 3** |
| ORM / Banco de Dados | JPA / Hibernate / **PostgreSQL** |
| Segurança | **Spring Security 6** + **JWT** (JSON Web Token) |
| Armazenamento de Mídia | **Supabase Storage** (via API REST com `WebClient`) |
| Containerização | **Docker** / **Docker Compose** |
| Build | Maven |
| Testes | JUnit 5 / AssertJ / Spring Test |
| Versionamento | Git + GitHub |

---

## 🧩 Arquitetura Aplicada

A aplicação segue uma arquitetura em 3 camadas (`Controller`, `Service`, `Repository`) com foco nos princípios **SOLID**.

-   **`Controller` (Camada de API):** Lida apenas com o roteamento HTTP. Não contém lógica de negócio.
-   **`Service` (Camada de Negócio):** Orquestra as regras (quem pode postar, como gerar o feed). Usa *Service-para-Service* para manter o encapsulamento.
-   **`Repository` (Camada de Dados):** Interfaces `JpaRepository` para acesso ao banco.
-   **`DTOs` (Data Transfer Objects):** Separação total dos dados de entrada (`CriacaoDTO`), saída (`ResponseDTO`) e Entidades, garantindo que dados sensíveis (senhas) nunca sejam expostos.
-   **`Mapper` (Padrão de Mapeamento):** Classes dedicadas para converter DTOs ↔ Entidades, mantendo os Services limpos.
-   **`GlobalExceptionHandler`:** Um "Xerife" (`@RestControllerAdvice`) que centraliza o tratamento de todos os erros (404, 403, 400, 409), mantendo os Controllers 100% limpos de `try-catch`.

---

## 🧩 Estrutura do Projeto

src/</br>
├── main/</br>
│ ├── .../mini_rede_social/</br>
│ │ ├── controller/ → (Endpoints REST)</br>
│ │ ├── service/    → (Regras de Negócio: FeedService, PostagemService...)</br>
│ │ ├── repository/ → (Interfaces JpaRepository)</br>
│ │ ├── model/      → (Entidades JPA: @Entity)</br>
│ │ ├── dto/        → (DTOs de Requisição e Resposta: Records)</br>
│ │ ├── mapper/     → (Conversores: PostagemMapper, UsuarioMapper...)</br>
│ │ ├── security/   → (SecurityConfig, JwtUtil, JwtAuthFilter...)</br>
│ │ ├── exception/  → (GlobalExceptionHandler, Exceções Customizadas)</br>
│ │ └── config/     → (WebClientConfig, SupabaseConfig)</br>
│ └── resources/</br>
│   ├── application.properties         → (Configuração local)</br>
│   └── application-docker.properties  → (Configuração para o Docker)</br>
└── test/</br>
└── ... → (Testes de Integração e Unitários)


---

---

## 🧱 Modelagem de Dados (JPA)

A modelagem separa dados de Autenticação (`UsuarioModel`) de dados Públicos (`PerfilModel`) usando um relacionamento 1:1, garantindo segurança.


### **Relacionamentos Principais:**
-   `Usuario` 1️⃣→1️⃣ `Perfil` (O Perfil é uma extensão do Usuário)
-   `Usuario` 1️⃣→N `Postagem` (Autoria)
-   `Usuario` 1️⃣→N `Comentario` (Autoria)
-   `Usuario` 1️⃣→N `Curtida`
-   `Postagem` 1️⃣→N `Comentario`
-   `Postagem` 1️⃣→N `Curtida`
-   `Usuario` N️↔N `Usuario` (Implementado pela entidade `SeguidorModel`)

---

## 🔐 Autenticação e Segurança

O sistema utiliza **Spring Security** com arquitetura **Stateless (Sem Sessão)**, usando **JWT (JSON Web Token)**.

### Fluxo de autenticação:
1.  O usuário envia `username` e `password` para `POST /auth/login`.
2.  O `AuthenticationManager` valida as credenciais (usando o `UserDetailsService` e `PasswordEncoder`).
3.  O `JwtUtil` gera um token JWT de curta duração.
4.  Para rotas protegidas (ex: `POST /api/postagens`), o token deve ser enviado no cabeçalho `Authorization` como `Bearer [TOKEN]`.
5.  O filtro `JwtAuthFilter` intercepta, valida o token e autentica o usuário na requisição.

---

## 🚀 Endpoints Principais (API REST)

### 👤 Autenticação (`/auth`)
| Método | Endpoint | Descrição | Status |
| :--- | :--- | :--- | :--- |
| `POST` | `/auth/register` | Registra um novo Usuário e Perfil (com DTOs). | ✅ |
| `POST` | `/auth/login` | Realiza login e retorna o Token JWT. | ✅ |

### 🛂 Status (Público)
| Método | Endpoint | Descrição | Status |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/status` | Health Check (Verifica se a API está no ar). | ✅ |

### 🖼️ Postagens (`/api/postagens`)
| Método | Endpoint | Descrição | Status |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/postagens` | Cria uma nova postagem (Requer `form-data` com imagem). | ✅ |
| `GET` | `/api/postagens/feed` | **(Feed Principal)** Lista postagens de quem você segue (Paginado). | ✅ |
| `GET` | `/api/postagens/{id}` | Retorna detalhes de uma postagem (com `PostagemResponseDTO`). | ✅ |
| `PUT` | `/api/postagens/{id}` | Atualiza imagem ou legenda (Somente autor). | ✅ |
| `DELETE`| `/api/postagens/{id}` | Remove uma postagem (Somente autor). | ✅ |

### ❤️ Interações Sociais (Curtir/Comentar)
| Método | Endpoint | Descrição | Status |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/postagens/{postId}/curtir` | Curte uma postagem (Trata 409 se já curtiu). | ✅ |
| `DELETE`| `/api/postagens/{postId}/descurtir` | Descurte uma postagem. | ✅ |
| `POST` | `/api/postagens/{postId}/comentarios` | Adiciona um comentário a um post. | ✅ |
| `GET` | `/api/postagens/{postId}/comentarios` | Lista comentários de um post. | ✅ |
| `DELETE`| `/api/comentarios/{id}` | Deleta um comentário (Autor ou Dono do post). | ✅ |

### 👥 Perfis e Seguidores (`/api/usuarios`, `/api/perfis`)
| Método | Endpoint | Descrição | Status |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/perfis/{username}` | Retorna o perfil público de um usuário (com `PerfilResponseDTO`). | ✅ |
| `PUT` | `/api/perfis` | Atualiza o *seu próprio* perfil (via Token JWT). | ✅ |
| `POST` | `/api/usuarios/{username}/seguir` | Segue um usuário. | ✅ |
| `DELETE`| `/api/usuarios/{username}/deixar-de-seguir` | Deixa de seguir um usuário. | ✅ |
| `GET` | `/api/usuarios/{username}/seguidores` | Lista os seguidores de um usuário. | ✅ |
| `GET` | `/api/usuarios/{username}/seguindo` | Lista quem um usuário está seguindo. | ✅ |

---

## 🧰 Configuração e Execução (Docker)

Este projeto é 100% containerizado usando **Docker Compose**. Ele sobe a API, o Banco de Dados (Postgres) e o Admin (PgAdmin) em uma rede isolada.

### 🔧 Pré-requisitos
-   Java 21+ (Para desenvolvimento local, se não usar Docker)
-   Maven 3+
-   **Docker** e **Docker Compose** (Instalados e rodando)

### ▶️ Executar o Projeto com Docker (Recomendado)

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/fernandosantos01/mini-rede-social.git](https://github.com/fernandosantos01/mini-rede-social.git)
    cd mini-rede-social
    ```
2.  **Crie o arquivo `.env`:**
    Na raiz do projeto, crie um arquivo `.env` com suas senhas e chaves (use o `.env.example` como base).
    ```env
    # Credenciais do Banco
    POSTGRES_DB=minisocial_db
    POSTGRES_USER=admin
    POSTGRES_PASSWORD=adminpass
    
    # Credenciais do Supabase
    SUPABASE_URL=https://[...].supabase.co
    SUPABASE_KEY=[SUA_SERVICE_ROLE_KEY]
    SUPABASE_BUCKET=postagens
    
    # Credenciais do PgAdmin
    PGADMIN_EMAIL=admin@admin.com
    PGADMIN_PASSWORD=admin
    ```

3.  **Suba o Ambiente:**
    (O Docker irá compilar o Java, baixar o Postgres e subir tudo)
    ```bash
    docker-compose up --build
    ```
    *(Use `docker-compose up -d --build` para rodar em segundo plano)*.

4.  **Acesse os serviços:**
    -   **API (Testar no Postman):** `http://localhost:8080/`
    -   **PgAdmin (Ver o Banco):** `http://localhost:5050`
        (Email: `admin@admin.com`, Senha: `admin`)
        (Host do Servidor no PgAdmin: `db`)

### 💧 Limpar o Ambiente
```bash
docker-compose down


## 🧪 Testes
Para rodar os testes automatizados:
```bash
mvn test
```

## 📚 Documentação da API
Após iniciar o servidor, acesse:
```bash
http://localhost:8080/swagger-ui.html
```

## 🌱 Roadmap (Próximos Passos)

-   [x] CRUD de Usuário e Perfil (1:1)
-   [x] Autenticação JWT (Stateless)
-   [x] CRUD de Postagens com Upload (Supabase Storage)
-   [x] CRUD de Curtidas (N:N)
-   [x] CRUD de Comentários (com permissão)
-   [x] Sistema de Seguir/Seguidores (N:N)
-   [x] Feed Principal (`/feed`) com Paginação
-   [x] Containerização com Docker Compose
-   [x] Arquitetura Limpa (SOLID, DTOs, Mappers, GlobalExceptionHandler)
-   [ ] **Testes de Integração** com `@SpringBootTest` e `@WithMockUser`
-   [ ] Adicionar documentação com **Swagger/OpenAPI**
-   [ ] Deploy em ambiente cloud (ex: AWS, Fly.io)

## 🧑‍💻 Autor
José Fernando
Acadêmico de Ciência da Computação - UFPI
Desenvolvedor Back-End | Java & Spring Boot
Teresina - PI
LinkedIn: https://www.linkedin.com/in/fernandosantos00
E-mail: josefernandojosefernando.12@gmail.com

Projeto desenvolvido com foco em aprendizado, boas práticas e escalabilidade.
