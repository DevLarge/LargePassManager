package largepass;

import largepass.services.CryptoService;
import largepass.services.FileService;
import java.nio.file.Path;

public class AppContext {

    private CryptoService cryptoService;
    private FileService fileService;

    public AppContext() {
        this.cryptoService = new CryptoService();
        this.fileService = new FileService(Path.of(System.getProperty("user.home"), ".largepass"));
        fileService.ensureFilesExist();
    }

    public CryptoService getCryptoService() {
        return cryptoService;
    }
    public FileService getFileService() {
        return fileService;
    }
}
