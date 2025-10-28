# 🧭 Mini Rede Social

> Projeto desenvolvido por **José Fernando**, acadêmico de Ciência da Computação (UFPI) e desenvolvedor Back-End focado em Java com Spring Boot.

---

## 📘 Visão Geral

A **Mini Rede Social** é uma aplicação desenvolvida com o objetivo de **simular o funcionamento básico de uma rede social moderna**, oferecendo recursos como:

- Criação de perfis de usuário  
- Postagens com texto e mídia  
- Sistema de curtidas e comentários  
- Seguir e deixar de seguir usuários  
- Autenticação e autorização via JWT  

O foco do projeto é aplicar **boas práticas de engenharia de software**, **padrões de arquitetura** e **segurança**, utilizando **Spring Boot** no back-end e integração com um banco de dados relacional.

---

## ⚙️ Tecnologias Utilizadas

| Categoria | Tecnologias |
|------------|--------------|
| Linguagem | Java 17 |
| Framework | Spring Boot 3 |
| ORM / Banco de Dados | JPA / Hibernate / PostgreSQL |
| Segurança | Spring Security + JWT |
| Build | Maven |
| Testes | JUnit 5 |
| Documentação | Swagger (OpenAPI) |
| Versionamento | Git + GitHub |

---

## 🧩 Estrutura do Projeto

src/ <br/>
├── main/ <br/>
│ ├── java/com/minirede/ <br/>
│ │ ├── controller/ → Endpoints REST <br/>
│ │ ├── service/ → Regras de negócio <br/>
│ │ ├── repository/ → Acesso ao banco de dados <br/>
│ │ ├── model/ → Entidades JPA <br/>
│ │ ├── dto/ → Objetos de transferência de dados <br/>
│ │ └── security/ → Autenticação e JWT <br/>
│ └── resources/ <br/>
│ ├── application.yml → Configurações do sistema <br/>
│ └── data.sql → Dados iniciais (seed) <br/>
└── test/ <br/>
└── ... → Testes unitários e de integração <br/>


---

## 🧱 Modelagem de Dados

### **Entidades Principais**
| Entidade | Descrição |
|-----------|------------|
| **User** | Representa o usuário da rede (nome, e-mail, senha, bio, imagem de perfil). |
| **Post** | Representa publicações feitas pelos usuários. |
| **Comment** | Comentários associados a um post. |
| **Like** | Curtidas realizadas pelos usuários em posts. |
| **Follow** | Relação entre seguidores e seguidos. |

### **Relacionamentos**
- `User` 1️⃣→N `Post`  
- `Post` 1️⃣→N `Comment`  
- `Post` 1️⃣→N `Like`  
- `User` N️⃣↔N `Follow`

---

## 🔐 Autenticação e Segurança

O sistema utiliza **Spring Security com JWT (JSON Web Token)** para garantir autenticação e autorização seguras.

### Fluxo de autenticação:
1. O usuário realiza login enviando `email` e `senha`.
2. O backend valida as credenciais.
3. É gerado um **token JWT** contendo as permissões do usuário.
4. O token deve ser enviado em todas as requisições autenticadas:


---

## 🧠 Lógica de Negócio

- Usuários podem **seguir** e **ser seguidos**.
- Um usuário pode **criar, editar e excluir** apenas seus próprios posts.
- É possível **curtir e comentar** publicações de outros usuários.
- O **feed** exibe publicações dos usuários seguidos.

---

## 🚀 Endpoints Principais

### 👤 Usuários
| Método | Endpoint | Descrição |
|--------|-----------|------------|
| `POST` | `/api/users/register` | Registra um novo usuário |
| `POST` | `/api/users/login` | Realiza login e retorna JWT |
| `GET` | `/api/users/{id}` | Retorna o perfil do usuário |
| `PUT` | `/api/users/{id}` | Atualiza dados do usuário |
| `POST` | `/api/users/{id}/follow` | Segue um usuário |
| `GET` | `/api/users/{id}/followers` | Lista seguidores |

---

### 📝 Postagens
| Método | Endpoint | Descrição |
|--------|-----------|------------|
| `GET` | `/api/posts` | Lista todas as postagens |
| `POST` | `/api/posts` | Cria uma nova postagem |
| `GET` | `/api/posts/{id}` | Retorna detalhes de uma postagem |
| `DELETE` | `/api/posts/{id}` | Remove uma postagem (somente autor/admin) |

---

### 💬 Comentários
| Método | Endpoint | Descrição |
|--------|-----------|------------|
| `POST` | `/api/comments` | Cria um novo comentário |
| `GET` | `/api/comments/post/{postId}` | Lista comentários de uma postagem |

---

### ❤️ Curtidas
| Método | Endpoint | Descrição |
|--------|-----------|------------|
| `POST` | `/api/likes/{postId}` | Curte ou descurte um post |
| `GET` | `/api/likes/post/{postId}` | Lista curtidas de um post |

---

## 🧰 Configuração e Execução

### 🔧 Pré-requisitos
- Java 17+
- Maven 3+
- PostgreSQL 15+

### ▶️ Executar o Projeto

```bash
git clone https://github.com/fernandosantos01/mini-rede-social.git
cd mini-rede-social
mvn spring-boot:run
```
Acesse:
👉 http://localhost:8080/



## ⚙️ Configuração do Banco de Dados
```bash
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/socialdb
    username: postgres
    password: sua_senha
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

-------


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

## 🌱 Roadmap
- CRUD de usuários e postagens
- Autenticação JWT
- Sistema de curtidas e comentários
- Upload de imagens com AWS S3 (futuro)
- Chat em tempo real com WebSocket
- Feed personalizado
- Deploy em ambiente cloud

## 🧑‍💻 Autor
José Fernando
Acadêmico de Ciência da Computação - UFPI
Desenvolvedor Back-End | Java & Spring Boot
Teresina - PI
LinkedIn: https://www.linkedin.com/in/fernandosantos00
E-mail: fernandosantos01@gmail.com

Projeto desenvolvido com foco em aprendizado, boas práticas e escalabilidade.
