package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        MyConnection mc = MyConnection.getMyConnection();
        Connection c = mc.getConnection();
        Parent root = FXMLLoader.load(getClass().getResource("PageDeGarde.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Risque Zéro");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(720);
        primaryStage.setMinWidth(1080);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}