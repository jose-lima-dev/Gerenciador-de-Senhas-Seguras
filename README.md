# Gerenciador de Senhas Seguras

## Descrição

Aplicação em Java para armazenar e gerenciar senhas de forma segura. Permite cadastro, criptografia, autenticação com 2FA, geração de senhas fortes, verificação de vazamentos e persistência em banco de dados SQLite.

---

## Funcionalidades

* **Cadastro de senhas:** Armazenamento de credenciais (serviço, usuário, senha) com criptografia AES.
* **Autenticação:** Login com senha mestre + autenticação de dois fatores (2FA) via TOTP (Google Authenticator).
* **Geração de senhas seguras:** Gerador de senhas aleatórias fortes personalizável pelo usuário.
* **Verificação de vazamento:** Integração com API externa para checar se a senha foi exposta em vazamentos.
* **Banco de dados:** Persistência das senhas em banco SQLite local.
* **Exclusão de senhas:** Remoção de senhas específicas pelo ID.

---

## Tecnologias Utilizadas

* Java 21
* SQLite (via JDBC)
* Google Authenticator (biblioteca `com.warrenstrange:googleauth`)
* Apache Commons Codec (para hash e criptografia)
* API HaveIBeenPwned (para verificação de vazamentos)
* Maven (gerenciamento de dependências e build)

---

## Estrutura do Projeto

```

src/
├── main/
│    ├── java/
│    │    ├── controller/          # Lógica de controle do app
│    │    ├── dao/                 # Acesso a banco de dados
│    │    ├── model/               # Modelos de dados
│    │    ├── service/             # Serviços (autenticação, criptografia, etc)
│    │    └── util/                # Helpers e utilitários (ex: conexão DB)
│    └── resources/
└── test/                         # Testes unitários (opcional)

````

---

## Como Rodar

1. **Pré-requisitos:**

    * Java JDK 21 instalado
    * Maven instalado

2. **Clonar o repositório:**

   ```bash
   git clone https://github.com/jose-lima-dev/Gerenciador-de-Senhas-Seguras
   cd Gerenciador-de-Senhas-Seguras
````

3. Compilar e Executar:

   Execute a aplicação iniciando a classe `app.App`.

   > A execução pode ser feita via sua IDE (como IntelliJ ou Eclipse), ou utilizando um comando Maven que chame a classe principal do aplicativo.

---

## Uso

* Ao iniciar o app, será criado automaticamente um novo banco de dados local (`senhas.db`).
* A senha mestre padrão é: `senha123`.
* Configure o 2FA escaneando o QR Code exibido no terminal ou digitando a chave no Google Authenticator.
* O código 2FA *muda a cada execução* do aplicativo.
* Utilize o menu para adicionar, listar, gerar, verificar e excluir senhas.

---

## Configuração do 2FA

* Na primeira autenticação, o app exibe uma chave secreta e um link QR Code.
* Abra o app Google Authenticator no celular e escaneie o QR Code ou insira manualmente a chave secreta.
* Digite o código gerado pelo app para concluir a autenticação.

> ⚠️ O código 2FA **é gerado dinamicamente a cada execução**. Para cada nova sessão, um novo QR Code será apresentado.

---

## Observações

* **Senha mestre:** Está fixa como `"senha123"` para simplificação. Em produção, implemente armazenamento e verificação segura.
* **Criptografia:** Senhas são criptografadas com AES antes de armazenar no banco.
* **Segurança:** Nunca armazene senhas em texto puro!
* **Banco de dados:** O arquivo `senhas.db` é gerado automaticamente ao iniciar o app.
* **Melhorias futuras:** Suporte a sincronização na nuvem, modo offline, interface gráfica Swing/JavaFX.

---

## Contato

Para dúvidas, sugestões ou contribuições: [joselimaprofissional18@gmail.com](mailto:joselimaprofissional18@gmail.com)

---

## Licença

Projeto aberto sob a licença MIT. Veja o arquivo LICENSE para detalhes.
