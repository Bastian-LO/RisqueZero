package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.data.users.UserAdmin;

import java.io.IOException;

public class PageAccueilAdminController {
    @FXML
    public Button deconnexionButton;
    @FXML
    public Button secouristesButton;
    @FXML
    public Label idProfilLabel;
    @FXML
    public Label nomPrenomLabel;
    @FXML
    public Label mailLabel;
    @FXML
    public Label phoneLabel;
    @FXML
    public Button DPSButton;
    @FXML
    public Label idEvenementLabel;
    @FXML
    public Label sportLabel;
    @FXML
    public Label siteLabel;
    @FXML
    public Label dateLabel;
    @FXML
    public Label nbSpectateursLabel;
    
    public UserAdmin admin;

    public void setUser(UserAdmin admin){
        this.admin = admin;
        updateUI();
    }

    private void updateUI() {
        updateProfileLabels();
        updateDPS();
    }

    private void updateDPS() {
    }

    private void updateProfileLabels() {

    }

    public void deconnexionPage(ActionEvent actionEvent) {
        try {
            // Chargement de la deuxième interface depuis second.fxml
            Parent connexionRoot = FXMLLoader.load(getClass().getResource("../resources/fxml/PageConnexion.fxml"));

            // Création de la nouvelle scène
            Scene connexionScene = new Scene(connexionRoot);

            // Récupération de la fenêtre actuelle
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.setScene(connexionScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void secouristePage(ActionEvent actionEvent) {
        try {
            // Chargement de la deuxième interface depuis second.fxml
            Parent secouristeRoot = FXMLLoader.load(getClass().getResource("../resources/fxml/PageGestionSecouristes.fxml"));

            // Création de la nouvelle scène
            Scene secouristeScene = new Scene(secouristeRoot);

            // Récupération de la fenêtre actuelle
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.setScene(secouristeScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dpsPage(ActionEvent actionEvent) {
        try {
            // Chargement de la deuxième interface depuis second.fxml
            Parent secouristeRoot = FXMLLoader.load(getClass().getResource("../resources/fxml/PageDPS.fxml"));

            // Création de la nouvelle scène
            Scene secouristeScene = new Scene(secouristeRoot);

            // Récupération de la fenêtre actuelle
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.setScene(secouristeScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
