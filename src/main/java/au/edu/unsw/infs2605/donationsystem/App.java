package au.edu.unsw.infs2605.donationsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;


/**
 * JavaFX App
 **/
public class App extends Application {
    
    private Database database = new Database();
    
    private static Scene scene;
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        
        database.setupDatabase();
        
        stage.setTitle("Donation Management System");
        Scene scene = new Scene(loadFXML("app"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    
    public static void main(String[] args) {
        Application.launch(args);
    }

}