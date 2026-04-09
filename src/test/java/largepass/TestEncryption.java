package largepass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import largepass.util.Encryption;

public class TestEncryption {

    private Encryption encryption;

    @BeforeEach
    void setUp() {
        this.encryption = new Encryption();
    }

    @Test
    void masterPassEncrypted() {
        String masterPassBeforeEncryption = "Master passord123";
        String masterPassAfterEncryption = encryption.encrypt(masterPassBeforeEncryption, masterPassBeforeEncryption);
        assertNotEquals(masterPassBeforeEncryption, masterPassAfterEncryption);
    }

    @Test
    void masterPassAfterDecryptedEqualsMasterPassBeforeEncryption() {
        String m = "Master passord";
        String masterPassEncrypted = encryption.encrypt(m, m);
        String masterPassDecrypted = encryption.decrypt(masterPassEncrypted, m);
        assertEquals(m, masterPassDecrypted);
    }

    @Test
    void encryptedPayloadUsesSaltIvCiphertextFormat() {
        String encrypted = encryption.encrypt("hello", "secret");
        String[] parts = encrypted.split(":");

        assertEquals(3, parts.length);
        assertTrue(parts[0].length() > 0);
        assertTrue(parts[1].length() > 0);
        assertTrue(parts[2].length() > 0);
    }

    @Test
    void encryptingSameInputTwiceProducesDifferentCiphertext() {
        String plain = "same text";
        String password = "same password";

        String encrypted1 = encryption.encrypt(plain, password);
        String encrypted2 = encryption.encrypt(plain, password);

        assertNotEquals(encrypted1, encrypted2);
    }

    @Test
    void decryptThrowsWhenPasswordIsWrong() {
        String encrypted = encryption.encrypt("top-secret", "correct-password");

        assertThrows(RuntimeException.class, () -> encryption.decrypt(encrypted, "wrong-password"));
    }

    @Test
    void decryptThrowsWhenFormatIsInvalid() {
        assertThrows(RuntimeException.class, () -> encryption.decrypt("invalid-data", "password"));
    }

    @Test
    void encryptionSupportsUnicodeRoundTrip() {
        String plain = "P@ssw0rd med norske tegn: æøå";
        String password = "hovedpassord";

        String encrypted = encryption.encrypt(plain, password);
        String decrypted = encryption.decrypt(encrypted, password);

        assertEquals(plain, decrypted);
    }
    
}