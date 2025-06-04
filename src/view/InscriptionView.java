package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javax.swing.border.Border;

public class InscriptionView extends Application {
    private TextField nomField;
    private TextField prenomField;
    private TextField emailField;
    private TextField jourNaissanceField;
    private TextField moisNaissanceField;
    private TextField anneeNaissanceField;
    private TextField numero;
    private TextField nationaliteField;
    private Button inscriptionButton;


    @Override
    public void start(Stage primaryStage) {
        // Initialize the UI components
        nomField = new TextField();
        nomField.setPromptText("ex: Pascal");
        prenomField = new TextField();
        prenomField.setPromptText("ex: Pierre");
        emailField = new TextField();
        emailField.setPromptText("ex: pierrepascal@example.com");
        jourNaissanceField = new TextField();
        jourNaissanceField.setPromptText("ex: 07");
        moisNaissanceField = new TextField();
        moisNaissanceField.setPromptText("ex: 10");
        anneeNaissanceField = new TextField();
        anneeNaissanceField.setPromptText("ex: 1990");
        numero = new TextField();
        numero.setPromptText("ex: 6 ** ** ** **");
        nationaliteField = new TextField();
        nationaliteField.setPromptText("ex: Français");
        inscriptionButton = new Button("S'inscrire");

        VBox gauchePane = new VBox(10);

        BorderPane root = new BorderPane();
        // Pane pour la moitié droite
        VBox droitePane = new VBox();
        droitePane.setStyle("-fx-background-color: lightblue;");
        droitePane.getChildren().add(new Label("Zone à droite (50%)"));

        // Empêche le Pane d'occuper toute la largeur par défaut
        droitePane.setMinWidth(0); // important pour pouvoir le redimensionner dynamiquement

        // Ajout au côté droit
        root.setRight(droitePane);

        Scene scene = new Scene(root, 800, 400);

        // Ajuster la taille du Pane droit à 50% de la largeur totale
        droitePane.prefWidthProperty().bind(scene.widthProperty().multiply(0.5));
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            double largeurTotale = newVal.doubleValue();
            droitePane.setPrefWidth(largeurTotale / 2);
        });

        primaryStage.setScene(scene);
        // Code to set up the primary stage and scene goes here
        primaryStage.setTitle("Inscription");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
