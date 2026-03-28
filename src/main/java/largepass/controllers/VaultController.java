package largepass.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import largepass.AppContext;
import largepass.Entry;
import largepass.managers.VaultManager;

public class VaultController {

    private AppContext appContext;

    public VaultController(AppContext appContext) {
        this.appContext = appContext;
    }

    @FXML
    private ListView<Entry> listPasswords;

    @FXML
    private Menu menuFile;

    @FXML
    private void initialize() {

        // 1. decrypt database and add to List<Entry> 
        //          with VaultManager#generateDecryptedEntryList : List<Entry>
        // 2. use the in memory list for all changes edits etc. 
        // 3. when done encrypt the entire List<Entry> and add to the vault.db file with 
        //          with VaultManager#generateEncryptedVault : encrypted vault.db

        // never change database file encryption

        // on close clear memory ????



    
        listPasswords.setItems(VaultManager.getPasswordList());
    }

    @FXML
    private void generateTestPasswords() {
        // gen test passwords and try to display them in list
    }



    
}
