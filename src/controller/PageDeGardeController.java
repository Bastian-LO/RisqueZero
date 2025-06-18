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

    // Lien avec le bouton (fx:id = "goButton" dans SceneBuilder)
    @FXML
    private Button inscriptionButton;

    // Méthode déclenchée quand on clique sur le bouton (nommée "goToSecondWindow" dans On Action)
    @FXML
    public void test(ActionEvent event) {
        try {
            // Chargement de la deuxième interface depuis second.fxml
            Parent secondRoot = FXMLLoader.load(getClass().getResource("../../class/view/PageInscription.fxml"));

            // Création de la nouvelle scène
            Scene secondScene = new Scene(secondRoot);

            // Récupération de la fenêtre actuelle
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Changement de la scène
            currentStage.setScene(secondScene);
            currentStage.setTitle("Seconde Fenêtre");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
