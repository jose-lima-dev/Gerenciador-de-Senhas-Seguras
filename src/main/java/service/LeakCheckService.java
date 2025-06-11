package service;

import util.HttpClientUtil;

import java.security.MessageDigest;

/**
 * Serviço responsável por verificar se uma senha já foi exposta em vazamentos,
 * usando a API do HaveIBeenPwned (modo "k-anonimato").
 */
public class LeakCheckService {

    /**
     * Verifica se a senha foi vazada consultando a API HaveIBeenPwned.
     * Utiliza SHA-1 da senha e verifica parcialmente (prefixo/sufixo) para preservar privacidade.
     *
     * @param password A senha a ser verificada.
     * @return true se a senha estiver na lista de vazamentos; false caso contrário.
     */
    public boolean isPasswordLeaked(String password) {
        try {
            // Gera hash SHA-1 da senha (em maiúsculas, como exigido pela API)
            String sha1 = sha1Hex(password).toUpperCase();

            // Divide o hash em prefixo (5 primeiros caracteres) e sufixo (restante)
            String prefix = sha1.substring(0, 5);
            String suffix = sha1.substring(5);

            // Consulta a API apenas com o prefixo (anonimato preservado)
            String response = HttpClientUtil.get("https://api.pwnedpasswords.com/range/" + prefix);

            // A API retorna uma lista de sufixos e quantas vezes foram encontrados
            String[] lines = response.split("\\r?\\n");
            for (String line : lines) {
                String[] parts = line.split(":");

                // Compara o sufixo da senha com os sufixos da lista
                if (parts[0].equalsIgnoreCase(suffix) && Integer.parseInt(parts[1]) > 0) {
                    return true; // senha vazada
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao consultar API HaveIBeenPwned: " + e.getMessage());
        }

        return false; // não encontrada na lista
    }

    /**
     * Converte uma string em um hash SHA-1 hexadecimal.
     *
     * @param input Texto para ser hasheado.
     * @return Hash SHA-1 em formato hexadecimal.
     * @throws Exception se ocorrer erro na geração do hash.
     */
    private String sha1Hex(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] data = md.digest(input.getBytes("UTF-8"));

        // Converte os bytes em string hexadecimal
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
