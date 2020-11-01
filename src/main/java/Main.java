/**
 * Main class used for GUI Scene setup.
 *
 * @author Chris Frank
 */

import javafx.application.Application;  // Entry point for Java FX applications
import javafx.fxml.FXMLLoader;          // Needed for Parent root to connect to fxml file
import javafx.scene.Parent;             // Needed for Parent root to connect to fxml file
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    // Create GUI Scene window
    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setTitle("Production");        // Set title for primary scene
        primaryStage.setScene(scene);               // Load primaryStage "startUp" Scene object
        primaryStage.setResizable(false);           // Restrict access to window sizing
        primaryStage.show();                        // Display primaryStage
    }
}