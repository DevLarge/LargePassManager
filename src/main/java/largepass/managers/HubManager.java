package largepass.managers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import largepass.AppContext;
import largepass.controllers.HubController;
import largepass.controllers.VaultController;

/**
 * Handles logic for the {@link HubController} class. 
 */
public class HubManager {

    private AppContext appContext;
    private HubController hubController;
    private int remainingTries = 3;

    public HubManager(AppContext appContext, HubController hubController) {
        this.appContext = appContext;
        this.hubController = hubController;
    }

    /**
     * If the masterpass gets accepted this method fires and changes screen to the vault. 
     * @param event needed from the Controller class to change stage. 
     */
    private void masterPassAccepted(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(HubController.class.getResource("/largepass/Vault.fxml"));
            VaultController vaultController = new VaultController(appContext);
            loader.setController(vaultController);

            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Vault");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Could not load vault scene " + e);
        }
    }

    /**
     * Fires if the masterpass is wrong. If the masterpass is wrong 3 times
     * the database will delete itself, and close the screen. 
     * @param event needed from the controller class to close stage. 
     */
    private void masterPassDenied(ActionEvent event) {
        if (this.remainingTries == 0) {
            appContext.getFileService().loadFiles();
            appContext.getFileService().deleteSensitiveFiles();
            appContext.getFileService().log("Deleted the database because of wrong tries!");
            hubController.labelAccountStatus.setText("boom 0 remaining. Deleted the database...");

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            Platform.exit();
        } else {
            hubController.labelAccountStatus.setText("Wrong master password. Self-destructing in " + String.valueOf(remainingTries));
            remainingTries--;
        }
        
    }

    /**
     * Checks if the master pass inputted in the textfield is correct to the 
     * decrypted version of the string inside the master.key file. 
     * @param masterPassIn the inputted master pass. 
     * @param event pass into other methods that need the event. 
     */
    public void validateMasterPass(String masterPassIn, ActionEvent event) {
        if (appContext.getFileService().isFirstTime()) {
            String encryptedMasterPass = appContext.getCryptoService().getEncryption().encrypt(masterPassIn, masterPassIn);
            appContext.getFileService().writeString(appContext.getFileService().getMasterPassFile(), encryptedMasterPass);
            appContext.getCryptoService().setMasterPassword(masterPassIn);
            masterPassAccepted(event);
        } else {
            appContext.getFileService().loadFiles();
            try {
                String decryptedMasterPass = appContext.getCryptoService().getEncryption().decrypt(Files.readString(appContext.getFileService().getMasterPassFile().toPath(), StandardCharsets.UTF_8), masterPassIn);
                if (decryptedMasterPass.equals(hubController.passwordFieldEnter.getText())) {
                    appContext.getCryptoService().setMasterPassword(hubController.passwordFieldEnter.getText());
                    masterPassAccepted(event);
                } else {
                    masterPassDenied(event);
                }
            } catch (RuntimeException e) {
                masterPassDenied(event);
            } catch (IOException e) {
                System.out.println("Something wrong with reading decrypted pass from file");
                e.printStackTrace();
            }
        }
    }
    
}
