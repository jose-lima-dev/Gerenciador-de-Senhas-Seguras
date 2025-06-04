Claro! Aqui está um README completo e profissional para o seu projeto **Gerenciador de Senhas Seguras**:

---

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
```

---

## Como Rodar

1. **Pré-requisitos:**

    * Java JDK 21 instalado
    * Maven instalado
    * Banco SQLite configurado (arquivo `senhas.db` será criado automaticamente)

2. **Clonar o repositório:**

   ```bash
   git clone https://github.com/jose-lima-dev/Gerenciador-de-Senhas-Seguras
   cd GerenciadorSenhasSeguras
   ```

3. **Build e execução:**

   ```bash
   mvn clean compile exec:java -Dexec.mainClass="App"
   ```

4. **Uso:**

    * Ao abrir o app, autentique-se com a senha mestre (padrão: `senha123`) e configure o 2FA.
    * Use o menu para adicionar, listar, gerar, verificar e deletar senhas.

---

## Configuração do 2FA

* Na primeira autenticação, o app exibe uma chave secreta e um link QR Code.
* Abra o app Google Authenticator no celular e escaneie o QR Code ou insira manualmente a chave secreta.
* Digite o código gerado pelo app para concluir a autenticação.

---

## Observações

* **Senha mestre:** Está fixa como `"senha123"` para simplificação. Em produção, implemente armazenamento e verificação segura.
* **Criptografia:** Senhas são criptografadas com AES antes de armazenar no banco.
* **Segurança:** Nunca armazene senhas em texto puro!
* **Banco de dados:** Verifique se o arquivo `senhas.db` está na pasta correta e acessível.
* **Melhorias futuras:** Suporte a sincronização na nuvem, modo offline, interface gráfica Swing/JavaFX.

---

## Contato

Para dúvidas, sugestões ou contribuições: [joselimaprofissional18@gmail.com](mailto:seu.email@example.com)

---

## Licença

Projeto aberto sob a licença MIT. Veja o arquivo LICENSE para detalhes.

---
