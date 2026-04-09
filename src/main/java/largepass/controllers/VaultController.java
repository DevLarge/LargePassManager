package largepass.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import largepass.AppContext;
import largepass.Entry;
import largepass.managers.VaultManager;

public class VaultController {

    private AppContext appContext;
    private VaultManager vaultManager;

    public VaultController(AppContext appContext) {
        this.appContext = appContext;
        this.vaultManager = new VaultManager(appContext, this);
    }

    @FXML
    public ListView<Entry> listPasswords;

    @FXML
    public Label labelService;
    @FXML
    public Label labelUsername;
    @FXML
    public Label labelPassword;
    

    @FXML
    private void initialize() {
        appContext.getCryptoService().decryptVault();
        listPasswords.setItems(appContext.getCryptoService().getObservablePasswordList());
        FXCollections.sort(appContext.getCryptoService().getObservablePasswordList());

        if (listPasswords.getSelectionModel().getSelectedItem() == null) {
            vaultManager.updateEntryInfo(null);
        }

        listPasswords.getSelectionModel().selectedItemProperty().addListener((observable, oldEntry, newEntry) -> {
            this.vaultManager.updateEntryInfo(newEntry);
        });
    }

    @FXML
    private void openAddEntryWindow() {
        vaultManager.addEntryButton();
    }

    @FXML
    private void clickDeleteButton() {
        vaultManager.deleteEntryButton();
    }
    
}
