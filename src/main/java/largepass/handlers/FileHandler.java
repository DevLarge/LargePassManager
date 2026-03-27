package largepass.handlers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileHandler {

    public static File VAULT;
    public static File LOGGER;
    public static File MASTER_PASS;

    public FileHandler() {
    }

    public static void makeFiles() {
        Path appDir = Path.of(System.getProperty("user.home"), ".largepass");
        FileHandler.VAULT = appDir.resolve("vault.db").toFile();
        FileHandler.MASTER_PASS = appDir.resolve("master.key").toFile();
        FileHandler.LOGGER = appDir.resolve("logger.txt").toFile();
    }

    public static void loadFiles() {
        Path appDir = Path.of(System.getProperty("user.home"), ".largepass");
        FileHandler.VAULT = appDir.resolve("vault.db").toFile();
        FileHandler.MASTER_PASS = appDir.resolve("master.key").toFile();
        FileHandler.LOGGER = appDir.resolve("logger.txt").toFile();
    }

    public static void writeString(File file, String str) {
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

    private static boolean isEmpty(Path path) {
        try {
            return Files.size(path) == 0;
        } catch (IOException e) {
            return true;
        }
    }

    public static String getMasterPass() {
        try {
            return Files.readString(MASTER_PASS.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Could not read master password file", e);
        }
    }

    public static boolean isFirstTime() {
        Path appDir = Path.of(System.getProperty("user.home"), ".largepass");
        File masterFile = appDir.resolve("master.key").toFile();
        return !Files.exists(masterFile.toPath(), LinkOption.NOFOLLOW_LINKS) || isEmpty(masterFile.toPath());
    }

    public static void ensureFilesExist() {
        try {
            Files.createDirectories(VAULT.toPath().getParent());

            if (!VAULT.exists()) {
                Files.writeString(VAULT.toPath(), "", StandardOpenOption.CREATE_NEW);
            }
            if (!MASTER_PASS.exists()) {
                Files.writeString(MASTER_PASS.toPath(), "", StandardOpenOption.CREATE_NEW);
            }
            if (!LOGGER.exists()) {
                Files.writeString(LOGGER.toPath(), "", StandardOpenOption.CREATE_NEW);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create app files", e);
        }
    }
}
