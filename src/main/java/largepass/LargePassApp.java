package largepass;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import largepass.handlers.FileHandler;

import java.io.IOException;

public class LargePassApp extends Application {

    public static void main(String[] args) {
        FileHandler.makeFiles();
        FileHandler.ensureFilesExist();
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Large Password Manager");
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("Hub.fxml"))));
        primaryStage.show();
    }
}
