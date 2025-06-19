 package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;


public class Main extends Application {

    /**
     * Starts the application, loading the first page (PageDeGarde)
     * @param primaryStage the primary stage to display the application
     * @throws Exception if any error occurs while loading the page
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("../resources/fxml/PageDeGarde.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Risque Zéro");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(800);
        primaryStage.show();
    }

    /**
     * The main entry point for the application.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}