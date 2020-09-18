import javafx.application.Application;  // Entry point for Java FX applications
import javafx.fxml.FXMLLoader;          // Needed for Parent root to connect to fxml file
import javafx.scene.Parent;             // Needed for Parent root to connect to fxml file
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        Scene scene = new Scene(root, 300, 275);

        primaryStage.setTitle("Production");        // Set title for primary scene
        primaryStage.setScene(scene);               // Load primaryStage "startUp" Scene object
        primaryStage.show();                        // Display primaryStage
    }
}