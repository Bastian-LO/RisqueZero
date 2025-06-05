package view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;



public class PageDeGarde extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Cr\u00E9ation des \u00E9l\u00E9ments UI
        Text titre = new Text("Risque Z\u00E9ro");
        titre.setFont(Font.font("Poppins", 180));
        titre.setFill(Color.DARKBLUE);

        Text sousTitre = new Text("Risque Z\u00E9ro est la plateforme de gestion de DPS num\u00E9ro 1 du march\u00E9.\n" + //
                                  "Moins de probl\u00e8mes d’organisation, plus de vies sauv\u00E9es !");
        sousTitre.setFont(Font.font("Arial", 18));
        sousTitre.setStyle("-fx-background-color: #f0f1ff; -fx-text-fill: white; -fx-font-size: 14px;");

        // Bouton avec style CSS int\u00E9gr\u00E9
        Button startButton = new Button("Commencer");
        startButton.setStyle("-fx-background-color: #f0f1ff; -fx-text-fill: white; -fx-font-size: 14px;");
        startButton.setOnAction(e -> {
            System.out.println("L'application d\u00E9marre !");
            // Ici, tu peux charger la prochaine scène
        });

        // Optionnel : Logo (remplace "logo.png" par ton fichier)
        ImageView logo = new ImageView(new Image("file:logo.png"));
        logo.setFitWidth(100);
        logo.setPreserveRatio(true);

        // 2. Layout (VBox pour un alignement vertical)
        VBox root = new VBox(20, logo, titre, sousTitre, startButton);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 40px;");

        // 3. Configuration de la scène
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setTitle("Page de Garde");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}