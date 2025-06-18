package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class PageDeGarde extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("page de connexion.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Risque Zéro");
        primaryStage.setScene(scene);
        root.setScaleX(0.7);
        root.setScaleY(0.7);
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(800);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}