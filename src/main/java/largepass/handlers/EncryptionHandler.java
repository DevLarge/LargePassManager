package largepass.handlers;

import java.lang.Character.UnicodeScript;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.CharacterIterator;
import java.util.Base64;
import java.util.Random;
import java.util.stream.IntStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionHandler {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int SALT_LENGTH = 16; // 128 bit
    private static final int IV_LENGTH = 12; // 96 bit for GCM ?
    private static final int KEY_LENGTH = 256; // AES-256
    private static final int PBKDF2_ITERATIONS = 120_000; // 
    private static final int GCM_TAG_LENGTH = 128; // bits


    public static String encrypt(String plainText, String masterPassword) {
        try {
            byte[] salt = new byte[SALT_LENGTH];
            RANDOM.nextBytes(salt);

            SecretKey key = deriveKey(masterPassword, salt);

            byte[] iv = new byte[IV_LENGTH];
            RANDOM.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            // Store as: base64(salt):base64(iv):base64(ciphertext)
            return Base64.getEncoder().encodeToString(salt) + ":"
                    + Base64.getEncoder().encodeToString(iv) + ":"
                    + Base64.getEncoder().encodeToString(cipherText);

        } catch (Exception e) {
            throw new RuntimeException("Encoding failed", e);
        }
    }

    public static String decrypt(String encryptedData, String masterPassword) {
        try {
            String[] parts = encryptedData.split(":");
            if (parts.length != 3) throw new IllegalArgumentException("Invalid data format");

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] iv = Base64.getDecoder().decode(parts[1]);
            byte[] cipherText = Base64.getDecoder().decode(parts[2]);

            SecretKey key = deriveKey(masterPassword, salt);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            byte[] plainBytes = cipher.doFinal(cipherText);

            return new String(plainBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Could not decrypt", e);
        }
    }

    public static void run() {
        encrypt("MyPassword123", "MasterPass");

    }

    private static SecretKey deriveKey(String masterPassword, byte[] salt) throws Exception{
        PBEKeySpec spec = new PBEKeySpec(masterPassword.toCharArray(), salt, PBKDF2_ITERATIONS);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }

    

}
