package largepass;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import largepass.controllers.HubController;
import largepass.controllers.VaultController;

import java.io.IOException;

public class LargePassApp extends Application {

    private AppContext appContext;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        appContext = new AppContext();

        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Hub.fxml"));
        loader.setControllerFactory(type -> {
            if (type == HubController.class) {
                return new HubController(appContext);
            }
            try {
                return type.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Could not create controller: " + type, e);
            }
        });

        Parent root = loader.load();
        primaryStage.setTitle("Large Password Manager");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
