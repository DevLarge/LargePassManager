package largepass.managers;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import largepass.AppContext;
import largepass.bin.Encryption;
import largepass.controllers.HubController;
import largepass.controllers.VaultController;

public class HubManager {

    private AppContext appContext;
    private HubController hubController;
    private int remainingTries = 3;

    public HubManager(AppContext appContext, HubController hubController) {
        this.appContext = appContext;
        this.hubController = hubController;
    }

    private void masterPassAccepted(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(HubController.class.getResource("/largepass/Vault.fxml"));
            loader.setControllerFactory(type -> {
                if (type == VaultController.class) {
                    return new VaultController(appContext);
                }
                try {
                    return type.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Could not create controller: " + type, e);
                }
            });
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Vault");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Could not load vault scene " + e);
        }
    }

    private void masterPassDenied(ActionEvent event) {
        if (this.remainingTries == 0) {
            appContext.getFileService().loadFiles();
            appContext.getFileService().deleteSensitiveFiles();
            appContext.getFileService().writeString(appContext.getFileService().getLogger(), "Deleted the database because of wrong tries!");
            hubController.labelAccountStatus.setText("boom 0 remaining. Deleted the database...");

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            Platform.exit();
        } else {
            hubController.labelAccountStatus.setText("Wrong master password. Self-destructing in " + String.valueOf(remainingTries));
            remainingTries--;
        }
        
    }

    public void validateMasterPass(String masterPassIn, ActionEvent event) {
        if (appContext.getFileService().isFirstTime()) {
            String encryptedMasterPass = getEncryption().encrypt(masterPassIn, masterPassIn);
            appContext.getFileService().writeString(appContext.getFileService().getMasterPassFile(), encryptedMasterPass);
            masterPassAccepted(event);
        } else {
            appContext.getFileService().loadFiles();
            try {
                String decryptedMaster = getEncryption().decrypt(appContext.getFileService().getMasterPass(), masterPassIn);
                if (decryptedMaster.equals(hubController.passwordFieldEnter.getText())) {
                    masterPassAccepted(event);
                } else {
                    masterPassDenied(event);
                }
            } catch (RuntimeException e) {
                masterPassDenied(event);
            }
        }
    }

    private Encryption getEncryption() {
        return appContext.getCryptoService().getEncryption();
    }
    
}
