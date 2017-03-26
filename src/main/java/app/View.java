package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Mish.k.a on 19. 3. 2017.
 */
public class View extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/TechnicianDuty.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/technicianDuty.css");

        primaryStage.setScene(scene);
        primaryStage.setTitle("Technician Duty");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

}
