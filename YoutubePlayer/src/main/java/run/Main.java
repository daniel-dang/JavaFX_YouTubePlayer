package run;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/*
 * Generic convention to start a JavaFx application
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage){
        try {
            FXMLLoader loader = new FXMLLoader();
            Parent root =  loader.load(getClass().getClassLoader().getResource("layoutView.fxml"));
            Scene scene = new Scene(root);

            primaryStage.setTitle("YoutubePlayer");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
