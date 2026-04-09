package largepass;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import largepass.services.FileService;

public class TestFiles {

    private FileService fileService;
    
    @BeforeEach
    void setUp() {
        this.fileService = new FileService(Path.of(System.getProperty("user.home"), ".largepass"));
        fileService.deleteSensitiveFiles();
    }

    @Test
    void checkIfFilesAreCreatedAfterCreation() {
        assertEquals(fileService.getMasterPassFile().exists(), false);
        assertEquals(fileService.getVault().exists(), false);
        fileService.ensureFilesExist();
        assertEquals(fileService.getMasterPassFile().exists(), true);
        assertEquals(fileService.getVault().exists(), true);
    }

    @Test
    void checkIfSensitiveFilesAreDeletedAfterDeletion() {
        fileService.ensureFilesExist();
        fileService.deleteSensitiveFiles();
        assertEquals(fileService.getMasterPassFile().exists(), false);
        assertEquals(fileService.getVault().exists(), false);
    }

    @Test
    void checkIfFirstTimeIsFalseAfterMasterPasswordIsWritten() {
        fileService.ensureFilesExist();
        assertEquals(fileService.isFirstTime(), true);

        fileService.writeString(fileService.getMasterPassFile(), "my-master-password");

        assertEquals(fileService.isFirstTime(), false);
    }

    @Test
    void checkIfWriteStringWritesToVaultFile() throws IOException {
        fileService.ensureFilesExist();
        String expected = "encrypted-content";

        fileService.writeString(fileService.getVault(), expected);
        String actual = Files.readString(fileService.getVault().toPath(), StandardCharsets.UTF_8);

        assertEquals(expected, actual);
    }
    
}
