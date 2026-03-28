package largepass.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileService {

    private Path pathDir;
    private File vault;
    private File logger;
    private File masterPassFile;

    public FileService(Path pathDir) {
        this.pathDir = pathDir;
        loadFiles();
        ensureFilesExist();
    }

    public void loadFiles() {
        this.vault = pathDir.resolve("vault.db").toFile();
        this.masterPassFile = pathDir.resolve("master.key").toFile();
        this.logger = pathDir.resolve("logger.txt").toFile();
    }

    public void writeString(File file, String str) {
        try {
            Files.writeString(
                file.toPath(),
                str,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
            );
        } catch (IOException e) {
            throw new RuntimeException("Could not write to file " + file, e);
        }
    }

    private boolean isEmpty(Path path) {
        try {
            return Files.size(path) == 0;
        } catch (IOException e) {
            return true;
        }
    }

    public String getMasterPass() {
        try {
            return Files.readString(getMasterPassFile().toPath());
        } catch (IOException e) {
            throw new RuntimeException("Could not read master password file", e);
        }
    }

    public boolean isFirstTime() {
        File masterFile = pathDir.resolve("master.key").toFile();
        return !masterFile.exists() || isEmpty(masterFile.toPath());
    }

    public void deleteSensitiveFiles() {
        try {
            if (getVault() != null) {
                Files.deleteIfExists(getVault().toPath());
            }
            if (getMasterPassFile() != null) {
                Files.deleteIfExists(getMasterPassFile().toPath());
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not delete sensitive files", e);
        }
    }

    public void ensureFilesExist() {
        try {
            Files.createDirectories(getVault().toPath().getParent());

            if (!getVault().exists()) {
                Files.writeString(getVault().toPath(), "", StandardOpenOption.CREATE_NEW);
            }
            if (!getMasterPassFile().exists()) {
                Files.writeString(getMasterPassFile().toPath(), "", StandardOpenOption.CREATE_NEW);
            }
            if (!getLogger().exists()) {
                Files.writeString(getLogger().toPath(), "", StandardOpenOption.CREATE_NEW);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create app files", e);
        }
    }
    public File getVault() {
        return vault;
    }
    public File getMasterPassFile() {
        return masterPassFile;
    }
    public File getLogger() {
        return logger;
    }
    
}
