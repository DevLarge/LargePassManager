package largepass.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Service for controlling the files. 
 * Files are saved under directory .largepass in the user.home directory. 
 */
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

    /**
     * Loads all files to memory from the pathDirectory. 
     */
    public void loadFiles() {
        this.vault = pathDir.resolve("vault.db").toFile();
        this.masterPassFile = pathDir.resolve("master.key").toFile();
        this.logger = pathDir.resolve("logger.txt").toFile();
    }

    /**
     * Writes a string to a file. 
     * @param file the file to write to.  
     * @param str the string to write. 
     */
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

    /**
     * Checkc if the path is empty. 
     * @param path
     * @return {@link Boolean} true if it is empty. 
     */
    private boolean isEmpty(Path path) {
        try {
            return Files.size(path) == 0;
        } catch (IOException e) {
            return true;
        }
    }

    /**
     * Checks if it is the user's first time. 
     * @return {@link Boolean} whether the file does not exist or if the file is empty. 
     */
    public boolean isFirstTime() {
        File masterFile = pathDir.resolve("master.key").toFile();
        return !masterFile.exists() || isEmpty(masterFile.toPath());
    }

    /**
     * Delets all the files. 
     */
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

    /**
     * Ensures that the files exists and have been made. 
     * If not it creates them. 
     */
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

    /**
     * For debugging. Writes a string to the logger file. 
     * Replaces everytihng everytime. 
     * @param logString the string to log. 
     */
    public void log(String logString) {
        try {
            if (getLogger() == null) {
                loadFiles();
            }
            if (!getLogger().exists()) {
                getLogger().createNewFile();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(getLogger(), false))) {
                writer.write(logString);
            }
        } catch (IOException e) {
            System.out.println(e);
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
