package service;

import util.HttpClientUtil;

import java.security.MessageDigest;

public class LeakCheckService {

    public boolean isPasswordLeaked(String password) {
        try {
            String sha1 = sha1Hex(password).toUpperCase();

            String prefix = sha1.substring(0, 5);
            String suffix = sha1.substring(5);

            String response = HttpClientUtil.get("https://api.pwnedpasswords.com/range/" + prefix);

            // Verifica se o suffix está na lista retornada
            String[] lines = response.split("\\r?\\n");
            for (String line : lines) {
                String[] parts = line.split(":");
                if (parts[0].equalsIgnoreCase(suffix) && Integer.parseInt(parts[1]) > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao consultar API HaveIBeenPwned: " + e.getMessage());
        }
        return false;
    }

    private String sha1Hex(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] data = md.digest(input.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
