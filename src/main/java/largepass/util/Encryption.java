package largepass.util;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/** 
 * Provides an authentication based encryption for text data based on a password.
 * It uses an AES key from the master password using PBKDF2 with HMAC-SHA256. 
 * Output format is a single string containing salt, IV, and ciphertext encoded in Base64. 
 *  
 */

public class Encryption {

    private final SecureRandom RANDOM = new SecureRandom();
    private final int SALT_LENGTH = 16; // 128 bit
    private final int IV_LENGTH = 12; // 96 bit for GCM ?
    private final int KEY_LENGTH = 256; // AES-256
    private final int PBKDF2_ITERATIONS = 120_000; // 
    private final int GCM_TAG_LENGTH = 128; // bits


    /**
     * 
     * @param plainText
     * @param masterPassword
     * @return {@link String} formatted as: "salt:iv:ciphertext"
     */
    public String encrypt(String plainText, String masterPassword) {
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

    /**
     * 
     * @param encryptedData
     * @param masterPassword
     * @return {@link String} as a plantext string UTF-8. 
     */
    public String decrypt(String encryptedData, String masterPassword) {
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

    private SecretKey deriveKey(String masterPassword, byte[] salt) throws Exception{
        PBEKeySpec spec = new PBEKeySpec(masterPassword.toCharArray(), salt, PBKDF2_ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        spec.clearPassword();
        return new SecretKeySpec(keyBytes, "AES");
    }

    

}
