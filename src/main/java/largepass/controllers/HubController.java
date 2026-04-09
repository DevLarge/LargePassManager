package largepass.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import largepass.AppContext;
import largepass.managers.HubManager;

public class HubController {

    private AppContext appContext;
    private HubManager hubManager;

    public HubController(AppContext appContext) {
        this.appContext = appContext;
        this.hubManager = new HubManager(appContext, this);
    }

    @FXML
    public Label labelAccountStatus;

    @FXML
    public PasswordField passwordFieldEnter;

    @FXML
    private void initialize() {
        appContext.getFileService().loadFiles();
        if (appContext.getFileService().isFirstTime()) {
            labelAccountStatus.setText("First time setup: create your master password");
        } else {
            labelAccountStatus.setText("Welcome back, enter your master password");
        }
    }

    @FXML
    private void handleEnterKeyPressed(ActionEvent event) {
        hubManager.validateMasterPass(passwordFieldEnter.getText(), event);
    }

}
