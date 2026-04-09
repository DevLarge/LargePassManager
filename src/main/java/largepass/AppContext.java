package largepass;

import largepass.services.CryptoService;
import largepass.services.FileService;
import java.nio.file.Path;

/**
 * Central point of the app. 
 * Contains instances of all important services. 
 */
public class AppContext {

    private final CryptoService cryptoService;
    private final FileService fileService;

    public AppContext() {
        this.cryptoService = new CryptoService(this);
        this.fileService = new FileService(Path.of(System.getProperty("user.home"), ".largepass"));
    }

    public CryptoService getCryptoService() {
        return cryptoService;
    }

    public FileService getFileService() {
        return fileService;
    }
}
