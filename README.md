# Gerenciador de Senhas Seguras 🔒

## Sobre o Projeto

Um gerenciador de senhas seguro desenvolvido em Java que oferece armazenamento criptografado de senhas, autenticação de dois fatores (2FA), geração de senhas fortes e verificação de vazamentos. O projeto foi desenvolvido com foco em segurança e usabilidade.

## ✨ Funcionalidades

- 🔐 **Senha Mestra Personalizada**
   - Configure sua própria senha mestra na primeira execução
   - Requisitos mínimos de segurança (maiúsculas, minúsculas, números, caracteres especiais)

- 🔑 **Autenticação de Dois Fatores (2FA)**
   - Integração com Google Authenticator
   - QR Code e chave secreta para configuração
   - Proteção adicional para acesso ao sistema

- 📝 **Gerenciamento de Senhas**
   - Adicionar novas credenciais (serviço, usuário, senha)
   - Listar todas as senhas armazenadas
   - Remover senhas específicas (requer confirmação)
   - Criptografia AES para armazenamento seguro

- 🎲 **Geração de Senhas Seguras**
   - Gerador de senhas aleatórias
   - Personalizável (comprimento, tipos de caracteres)

- 🔍 **Verificação de Vazamentos**
   - Integração com API HaveIBeenPwned
   - Verifica se suas senhas foram expostas em vazamentos

## 🛠️ Tecnologias

- **Java 21** - Linguagem de programação
- **SQLite** - Banco de dados local
- **Maven** - Gerenciamento de dependências
- **Bibliotecas:**
   - `com.warrenstrange:googleauth` - Implementação 2FA
   - `org.xerial:sqlite-jdbc` - Driver SQLite
   - `commons-codec` - Criptografia e hash

## 📦 Instalação

### Pré-requisitos

- Java JDK 21
- Maven

### Passos para Instalação

1. Clone o repositório:
   ```bash
   git clone https://github.com/jose-lima-dev/Gerenciador-de-Senhas-Seguras
   cd Gerenciador-de-Senhas-Seguras
   ```

2. Compile o projeto (caso o "mvn" não seja reconhecido, pode pular para a próxima etapa):
   ```bash
   mvn clean package
   ```

3. Execute o JAR gerado:
   ```bash
   java -jar target/GerenciadorSenhasSeguras-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```

## 🚀 Primeiro Uso

1. **Configuração Inicial**
   - Na primeira execução, você será guiado para criar sua senha mestra
   - Siga os requisitos de segurança apresentados

2. **Configuração 2FA**
   - Instale o Google Authenticator no seu celular
   - Escaneie o QR Code apresentado ou insira a chave secreta manualmente
   - Digite o código de 6 dígitos para validar

3. **Menu Principal**
   - Escolha entre as opções disponíveis:
      1. Adicionar senha
      2. Listar senhas
      3. Gerar senha segura
      4. Verificar vazamento de senha
      5. Remover senha
      0. Sair

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   ├── controller/    # Lógica de controle
│   │   ├── dao/          # Acesso ao banco
│   │   ├── model/        # Modelos de dados
│   │   ├── service/      # Serviços (auth, crypto)
│   │   └── util/         # Utilitários
│   └── resources/        # Recursos
└── test/                # Testes unitários
```

## 🔒 Segurança

- Todas as senhas são criptografadas com AES antes do armazenamento
- Senha mestra armazenada com hash seguro e salt
- Autenticação de dois fatores obrigatória
- Confirmação de senha mestra para operações críticas
- Verificação de vazamentos antes do armazenamento

## 📝 Notas Importantes

- O banco de dados (`data/senhas.db`) é criado automaticamente
- A pasta `data/` contém arquivos de configuração - não apague!
- Mantenha sua chave 2FA em local seguro
- Faça backup regular do diretório `data/`

## 👥 Contribuição

Contribuições são bem-vindas! Para contribuir:

1. Faça um Fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📧 Contato

José Lima - [joselimaprofissional18@gmail.com](mailto:joselimaprofissional18@gmail.com)

## 📄 Licença

Este projeto está sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.
