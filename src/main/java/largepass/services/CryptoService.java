package largepass.services;

import java.nio.file.Files;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import largepass.AppContext;
import largepass.Entry;
import largepass.util.Encryption;

/**
 * Crypto service for handling all mechanicsms using the encryptions. 
 * 
 */
public class CryptoService {

    private AppContext appContext;
    private Encryption encryption;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String masterPassword;
    private ObservableList<Entry> observablePasswordList;

    public CryptoService(AppContext appContext) {
        this.appContext = appContext;
        this.encryption = new Encryption();
        this.observablePasswordList = FXCollections.observableArrayList();
    }

    public Encryption getEncryption() {
        return encryption;
    }

    /**
     * Decrypts the vault.db file to an ObservableList<Entry>. 
     * Uses Jackson JSON. 
     */
    public void decryptVault() {
        try {
            String encryptedJson = Files.readString(appContext.getFileService().getVault().toPath());

            if (encryptedJson == null || encryptedJson.isBlank()) {
                this.observablePasswordList.clear();
                return;
            }

            String decryptedJson = getEncryption().decrypt(encryptedJson, getMasterPass());
            List<Entry> entries = objectMapper.readValue(decryptedJson, 
                new TypeReference<List<Entry>>() {} );
            this.observablePasswordList.setAll(entries);
        } catch (Exception e) {
            System.out.println("Something went wrong with decryption of vault! ");
            e.printStackTrace();
        }
    }

    /**
     * Converts ObservableList<Entry> to a plaintext JSON using {@link ObjectMapper}
     * then encrypting the json string and adds it to vault.db file.  
     */
    public void encryptVault() {
        if (getMasterPass().isEmpty()) throw new IllegalStateException("Noe galt med masterpass = null?");
        try {
            String json = objectMapper.writeValueAsString(this.observablePasswordList);
            String encryptedJson = getEncryption().encrypt(json, getMasterPass());
            appContext.getFileService().writeString(appContext.getFileService().getVault(), encryptedJson);

        } catch (JsonProcessingException e) {
            System.out.println("Could not encrypt vault! ");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something else happened during encryption of vault!");
            e.printStackTrace();
        }
    }

    public ObservableList<Entry> getObservablePasswordList() {
        return this.observablePasswordList;
    }

    /**
     * Returns a plaintext masterpass from memory. 
     * @return {@link String} 
     */
    public String getMasterPass() {
        if (this.masterPassword == null || this.masterPassword.isEmpty()) {
            throw new IllegalStateException("The user must set a password to continue!");
        }
        return this.masterPassword;
    }

    public void setMasterPassword(String masterPassword) {
        this.masterPassword = masterPassword;
    }

    
}
