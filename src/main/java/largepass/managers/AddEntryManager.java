package largepass.managers;

import javafx.collections.FXCollections;
import largepass.AppContext;
import largepass.Entry;
import largepass.controllers.AddEntryController;

/**
 * Handles logic for the AddEntryController. 
 */
public class AddEntryManager {

    private AppContext appContext;
    private AddEntryController addEntryController;

    public AddEntryManager(AppContext appContext, AddEntryController addEntryController) {
        this.addEntryController = addEntryController;
        this.appContext = appContext;
    }

    /**
     * Saves an entry with the values inputted into the textfields.
     * Saves to the ObservableList<Entry>, and encrypts the vault to add the encrypted entry
     * to the vault.db file. 
     */
    public void saveEntry() {
        if (addEntryController.titleField.getText().isEmpty() || addEntryController.usernameField.getText().isEmpty() || addEntryController.passwordField.getText().isEmpty()) {
            System.out.println("Title, Username, and Password cannot be empty");
            return;
        }

        Entry newEntry = new Entry(
            addEntryController.usernameField.getText(),
            addEntryController.passwordField.getText(),
            addEntryController.titleField.getText()  // service field
        );

        appContext.getCryptoService().getObservablePasswordList().add(newEntry);
        appContext.getCryptoService().encryptVault();
        FXCollections.sort(appContext.getCryptoService().getObservablePasswordList());

        addEntryController.stage.close();
    }

}
