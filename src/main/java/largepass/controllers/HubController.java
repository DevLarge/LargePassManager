package largepass.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.application.Platform;
import largepass.handlers.EncryptionHandler;
import largepass.handlers.FileHandler;

public class HubController {

    private int remainingTries = 3;

    @FXML
    private Label labelAccountStatus;

    @FXML
    private PasswordField passwordFieldEnter;

    @FXML
    private void initialize() {
        FileHandler.loadFiles();
        if (FileHandler.isFirstTime()) {
            labelAccountStatus.setText("First time setup: create your master password");
        } else {
            labelAccountStatus.setText("Welcome back, enter your master password");
        }
    }

    @FXML
    private void handleEnterKeyPressed(ActionEvent event) {
        validateMasterPass(passwordFieldEnter.getText(), event);
    }

    private void validateMasterPass(String masterPassIn, ActionEvent event) {
        if (FileHandler.isFirstTime()) {
            FileHandler.makeFiles();
            FileHandler.ensureFilesExist();
            String encryptedMasterPass = EncryptionHandler.encrypt(masterPassIn, masterPassIn);
            FileHandler.writeString(FileHandler.MASTER_PASS, encryptedMasterPass);
            masterPassAccepted(event);
        } else {
            FileHandler.loadFiles();
            try {
                String decryptedMaster = EncryptionHandler.decrypt(FileHandler.getMasterPass(), masterPassIn);
                if (decryptedMaster.equals(passwordFieldEnter.getText())) {
                    masterPassAccepted(event);
                } else {
                    masterPassDenied(event);
                }
            } catch (RuntimeException e) {
                masterPassDenied(event);
            }
        }
    }

    private void masterPassAccepted(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(HubController.class.getResource("/largepass/Vault.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Vault");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Could not load vault scene" + e);
        }
    }

    private void masterPassDenied(ActionEvent event) {
        if (this.remainingTries == 0) {
            FileHandler.loadFiles();
            FileHandler.deleteSensitiveFiles();
            FileHandler.writeString(FileHandler.LOGGER, "Deleted the database because of wrong tries!");
            labelAccountStatus.setText("boom 0 remaining. Deleted the database...");

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            Platform.exit();
        } else {
            this.labelAccountStatus.setText("Wrong master password. Self-destructing in " + String.valueOf(remainingTries));
            remainingTries--;
        }
        
    }

}
