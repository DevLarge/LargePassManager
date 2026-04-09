package largepass.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import largepass.AppContext;
import largepass.managers.AddEntryManager;

public class AddEntryController {

    private AddEntryManager addEntryManager;
    public Stage stage;

    @FXML
    public TextField titleField;

    @FXML
    public TextField usernameField;

    @FXML
    public TextField passwordField;


    public AddEntryController(AppContext appContext) {
        this.addEntryManager = new AddEntryManager(appContext, this);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void saveEntry() {
        addEntryManager.saveEntry();
    }

    @FXML
    private void cancelEntry() {
        stage.close();
    }
}
