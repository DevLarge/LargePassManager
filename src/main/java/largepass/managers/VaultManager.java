package largepass.managers;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import largepass.AppContext;
import largepass.Entry;
import largepass.controllers.AddEntryController;
import largepass.controllers.VaultController;

/**
 * Managing all methods for the {@link VaultController}. 
 */
public class VaultManager {

    private final String DEFAULT_SERVICE_TEXT = "Select an entry";
    private final String DEFAULT_VALUE_TEXT = "-";

    private AppContext appContext;
    private VaultController vaultController;

    public VaultManager(AppContext appContext, VaultController vaultController) {
        this.appContext = appContext;
        this.vaultController = vaultController;
    }

    /**
     * Updates the info in the view screen with the selected entry information. 
     * If no entry is selected, it uses the default final values. 
     * @param entry {@link Entry} to get data from. 
     */
    public void updateEntryInfo(Entry entry) {
        if (entry == null) {
            vaultController.labelService.setText(DEFAULT_SERVICE_TEXT);
            vaultController.labelUsername.setText(DEFAULT_VALUE_TEXT);
            vaultController.labelPassword.setText(DEFAULT_VALUE_TEXT);
            return;
        }

        vaultController.labelService.setText(entry.getService());
        vaultController.labelUsername.setText(entry.getUsername());
        vaultController.labelPassword.setText(entry.getPassword());
    }

    /**
     * Deletes the selected entry. 
     */
    public void deleteEntryButton() {
        Entry entryToDelete = vaultController.listPasswords.getSelectionModel().getSelectedItem();
        if (entryToDelete == null) {
            return;
        }

        appContext.getCryptoService().getObservablePasswordList().remove(entryToDelete);

        if (appContext.getCryptoService().getObservablePasswordList().isEmpty()) {
            updateEntryInfo(null);
        }
    }

    /**
     * Opens a new stage for adding an entry. 
     */
    public void addEntryButton() {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/largepass/AddEntry.fxml"));
            AddEntryController controller = new AddEntryController(appContext);
            loader.setController(controller);            

            Scene scene = new Scene(loader.load());
            
            Stage addEntryStage = new Stage();
            addEntryStage.setTitle("Add New Entry");
            addEntryStage.setScene(scene);
            
            addEntryStage.initModality(Modality.APPLICATION_MODAL);
            
            controller.setStage(addEntryStage);
            
            addEntryStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Add Entry window: " + e.getMessage());
        }
    }

    
    
}
