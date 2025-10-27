# Mini Rede Social

Este projeto consiste no desenvolvimento de uma mini-rede social com funcionalidades básicas de interação social.

## Funcionalidades

- **Autenticação de Usuários**: Registro e login de usuários
- **Perfis de Usuário**: Cada usuário tem seu próprio perfil com biografia editável
- **Publicações**: Usuários podem criar e deletar posts
- **Sistema de Seguidores**: Usuários podem seguir e deixar de seguir outros usuários
- **Feed Personalizado**: Veja posts de usuários que você segue
- **Explorar Usuários**: Descubra e siga novos usuários

## Tecnologias Utilizadas

- **Backend**: Flask (Python)
- **Banco de Dados**: SQLite com SQLAlchemy ORM
- **Autenticação**: Flask-Login
- **Frontend**: HTML, CSS (responsivo)

## Instalação e Execução

### Pré-requisitos

- Python 3.7 ou superior
- pip (gerenciador de pacotes Python)

### Passos para Instalação

1. Clone o repositório:
```bash
git clone https://github.com/fernandosantos01/mini-rede-social.git
cd mini-rede-social
```

2. Crie um ambiente virtual (recomendado):
```bash
python -m venv venv
```

3. Ative o ambiente virtual:
- No Windows:
```bash
venv\Scripts\activate
```
- No Linux/Mac:
```bash
source venv/bin/activate
```

4. Instale as dependências:
```bash
pip install -r requirements.txt
```

5. Execute a aplicação:
```bash
# Para desenvolvimento (com debug ativado)
export FLASK_ENV=development
python run.py

# Para produção (sem debug)
python run.py
```

6. Acesse a aplicação no navegador:
```
http://localhost:5000
```

## Como Usar

1. **Registre-se**: Crie uma conta com nome de usuário, email e senha
2. **Faça Login**: Entre com suas credenciais
3. **Explore**: Navegue pela lista de usuários e siga pessoas interessantes
4. **Publique**: Compartilhe seus pensamentos criando posts
5. **Interaja**: Veja o feed com posts de pessoas que você segue
6. **Edite seu Perfil**: Adicione uma biografia para se apresentar

## Estrutura do Projeto

```
mini-rede-social/
├── app/
│   ├── __init__.py          # Inicialização da aplicação
│   ├── models.py            # Modelos de banco de dados
│   ├── routes/
│   │   ├── auth.py          # Rotas de autenticação
│   │   └── main.py          # Rotas principais
│   ├── templates/           # Templates HTML
│   └── static/
│       └── css/
│           └── style.css    # Estilos CSS
├── run.py                   # Arquivo principal para executar a aplicação
├── requirements.txt         # Dependências do projeto
└── README.md               # Documentação
```

## Contribuindo

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues ou enviar pull requests.

## Licença

Este projeto é de código aberto e está disponível sob a licença MIT.
