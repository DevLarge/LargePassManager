package largepass.controllers;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import largepass.handlers.EncryptionHandler;
import largepass.handlers.FileHandler;

public class HubController {

    private File vault;
    private File logger;

    private int remainingTries = 3;

    @FXML
    private Label labelWelcome;

    @FXML
    private PasswordField passwordFieldMasterPass;

    @FXML
    private Button buttonSubmit;

    @FXML
    private void handleEnterKeyPressed(ActionEvent event) {
        validateMasterPass(passwordFieldMasterPass.getText(), event);
    }

    @FXML
    private void handleSubmitButtonClicked(ActionEvent event) {
        validateMasterPass(passwordFieldMasterPass.getText(), event);
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
            if (EncryptionHandler.decrypt(FileHandler.getMasterPass(), masterPassIn).equals(passwordFieldMasterPass.getText())) {
                masterPassAccepted(event);
            } else {
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
            this.labelWelcome.setText("Could not load vault scene");
        }
    }

    private void masterPassDenied(ActionEvent event) {
        if (this.remainingTries == 1) {
            labelWelcome.setText("boom 0 remaining, close app");
        } else {
            this.labelWelcome.setText("Wrong master password. Remaining tries " + String.valueOf(remainingTries));
            remainingTries--;
        }
        
    }

}
