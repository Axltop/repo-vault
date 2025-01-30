package com.vault.backend.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AESGCMEncryptionUtil {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int IV_SIZE = 12; // Recommended IV size for GCM
    private static final int TAG_LENGTH = 128; // Authentication tag length (bits)

    public static SecretKey generateKey(String encryptionKey) {
        byte[] keyBytes = Base64.getDecoder().decode(Base64.getEncoder().encodeToString(encryptionKey.getBytes()));
        return new SecretKeySpec(keyBytes, "AES");
    }

    public static String encrypt(String data, SecretKey key) throws Exception {
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[IV_SIZE];
        secureRandom.nextBytes(iv); // Generate random IV

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

        byte[] encryptedBytes = cipher.doFinal(data.getBytes());

        // Store IV + Ciphertext together (IV is needed for decryption)
        byte[] combined = new byte[IV_SIZE + encryptedBytes.length];
        System.arraycopy(iv, 0, combined, 0, IV_SIZE);
        System.arraycopy(encryptedBytes, 0, combined, IV_SIZE, encryptedBytes.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    public static String decrypt(String encryptedData, SecretKey key) throws Exception {
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);

        // Extract IV and ciphertext
        byte[] iv = new byte[IV_SIZE];
        byte[] ciphertext = new byte[decodedBytes.length - IV_SIZE];
        System.arraycopy(decodedBytes, 0, iv, 0, IV_SIZE);
        System.arraycopy(decodedBytes, IV_SIZE, ciphertext, 0, ciphertext.length);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);

        byte[] decryptedBytes = cipher.doFinal(ciphertext);
        return new String(decryptedBytes);
    }
}
