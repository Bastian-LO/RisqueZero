package view;

import controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.*;

public class View extends Application {

    private Controller controller;

    public static void main(String[] args){
        MyConnection mc = MyConnection.getMyConnection();
        Connection c = mc.getConnection();
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        controller = new Controller(this);

    }
}