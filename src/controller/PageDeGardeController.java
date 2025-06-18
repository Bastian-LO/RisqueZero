package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Node;

public class PageDeGardeController {

    @FXML
    public Button connexionButton;
    // Lien avec le bouton (fx:id = "goButton" dans SceneBuilder)
    @FXML
    private Button inscriptionButton;

    // Méthode déclenchée quand on clique sur le bouton (nommée "goToSecondWindow" dans On Action)
    @FXML
    public void inscriptionPage(ActionEvent event) {
        try {
            // Chargement de la deuxième interface depuis second.fxml
            Parent inscriptionRoot = FXMLLoader.load(getClass().getResource("../resources/fxml/PageInscription.fxml"));

            // Création de la nouvelle scène
            Scene inscriptionScene = new Scene(inscriptionRoot);

            // Récupération de la fenêtre actuelle
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Changement de la scène
            currentStage.setScene(inscriptionScene);
            currentStage.setTitle("Inscription");
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connexionPage(ActionEvent event) {
        try {
            // Chargement de la deuxième interface depuis second.fxml
            Parent connexionRoot = FXMLLoader.load(getClass().getResource("../resources/fxml/PageConnexion.fxml"));

            // Création de la nouvelle scène
            Scene connexionScene = new Scene(connexionRoot);

            // Récupération de la fenêtre actuelle
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Changement de la scène
            currentStage.setScene(connexionScene);
            currentStage.setTitle("Connexion");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
