## Como rodar com Docker

### Pré-requisitos
- Docker e Docker Compose instalados

### Passo a passo
1. Entrar na pasta do projeto:
```bash
cd /home/jhonhdev/Documentos/JavaProject/mini-rede-social/mini-rede-social
```

2. Criar o arquivo de ambiente a partir do exemplo:
```bash
cp env.example .env
```

3. Subir os serviços (app, Postgres e pgAdmin opcional):
```bash
docker compose up -d --build
```

4. Verificar se está tudo no ar:
```bash
docker compose ps
docker compose logs -f app
```

### Endereços
- API: `http://localhost:8080`
- Postgres: `localhost:5432`
- pgAdmin (opcional): `http://localhost:5050` (use email/senha do `.env`)

### Teste rápido
Ver se a aplicação está respondendo (401 indica que a segurança está ativa, o que é esperado):
```bash
curl -i http://localhost:8080
```

Fluxo mínimo (se a API expõe registro e login):
```bash
# Registrar usuário
curl -i -H "Content-Type: application/json" \
  -d '{"name":"Teste","email":"teste@local","password":"123456"}' \
  http://localhost:8080/api/users/register

# Login e capturar token (requer jq)
TOKEN=$(curl -s -H "Content-Type: application/json" \
  -d '{"email":"teste@local","password":"123456"}' \
  http://localhost:8080/api/users/login | jq -r '.token // .access_token // .jwt')
echo $TOKEN

# Chamar endpoint autenticado
curl -i -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/posts
```

### Comandos úteis
```bash
# Parar
docker compose down

# Reconstruir imagens
docker compose build --no-cache

# Ver logs
docker compose logs -f app
docker compose logs -f db
```

### Observações
- O perfil `docker` é ativado via `SPRING_PROFILES_ACTIVE=docker` no `docker-compose.yml`.
- As credenciais do banco vêm do arquivo `.env`.
- O Dockerfile usa Maven e Temurin JDK 21 para compilar/rodar.


