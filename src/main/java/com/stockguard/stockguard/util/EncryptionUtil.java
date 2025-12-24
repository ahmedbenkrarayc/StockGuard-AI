package com.stockguard.stockguard.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class EncryptionUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    private final SecretKeySpec secretKey;

    public EncryptionUtil(@Value("${app.encryption.key}") String encryptionKey) {
        // Ajuster la clé à 16 bytes pour AES-128
        byte[] keyBytes = encryptionKey.getBytes(StandardCharsets.UTF_8);
        byte[] validKey = new byte[16];

        int length = Math.min(keyBytes.length, validKey.length);
        System.arraycopy(keyBytes, 0, validKey, 0, length);

        // Remplir le reste avec des zéros si nécessaire
        for (int i = length; i < validKey.length; i++) {
            validKey[i] = 0;
        }

        this.secretKey = new SecretKeySpec(validKey, ALGORITHM);
    }

    public String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Erreur de chiffrement: " + e.getMessage(), e);
        }
    }

    public String decrypt(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Erreur de déchiffrement: " + e.getMessage(), e);
        }
    }

    public String encryptBigDecimal(BigDecimal value) {
        return encrypt(value.toString());
    }

    public BigDecimal decryptBigDecimal(String encryptedValue) {
        String decrypted = decrypt(encryptedValue);
        return new BigDecimal(decrypted);
    }
}
