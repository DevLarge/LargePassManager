package largepass.services;

import largepass.bin.Encryption;

public class CryptoService {

    private Encryption encryption;

    public CryptoService() {
        this.encryption = new Encryption();
    }

    public Encryption getEncryption() {
        return encryption;
    }

    // add encrypt whole file and add to list etc

    
}
