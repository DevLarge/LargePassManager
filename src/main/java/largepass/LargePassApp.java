package largepass;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import largepass.controllers.HubController;

import java.io.IOException;

/**
 * Main class of the app. 
 * Uses JavaFX to run, thereby extends application. 
 * Creates one singular instance of the {@link AppContext} class. 
 * 
 * Sets base scene. 
 */
public class LargePassApp extends Application {

    private AppContext appContext;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        appContext = new AppContext();


        FXMLLoader loader = new FXMLLoader(getClass().getResource("Hub.fxml"));
        
        HubController hubController = new HubController(appContext);
        loader.setController(hubController);

        Parent root = loader.load();
        primaryStage.setTitle("Large Password Manager");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
