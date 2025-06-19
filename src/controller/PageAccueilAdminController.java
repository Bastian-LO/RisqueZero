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
import model.data.persistence.DPS;
import model.data.service.DAOMngt;
import model.data.users.UserAdmin;

import java.io.IOException;
import java.util.List;

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
        List<DPS> dps = DAOMngt.getDPSDAO().findAll();

        if (dps == null || dps.isEmpty()) {
            idEvenementLabel.setText("Aucun événement pour l'instant.");
            sportLabel.setText("");
            siteLabel.setText("");
            dateLabel.setText("");
            return;
        }

        DPS nextDPS = dps.get(0);
        for ( DPS dps1 : dps) {
            if (dps1.getDateEvt().isBefore(nextDPS.getDateEvt())) {
                nextDPS = dps1;
            }
        }

        idEvenementLabel.setText(String.valueOf(nextDPS.getId()));
        sportLabel.setText(nextDPS.getSport().getNom());
        siteLabel.setText(nextDPS.getLieu().getNom());
        dateLabel.setText(nextDPS.getDateEvt().toString());
    }

    private void updateProfileLabels() {
        idProfilLabel.setText(String.valueOf(admin.getId()));
        mailLabel.setText(admin.getLogin());
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PageGestionSecouristes.fxml"));
            Parent secouristeRoot = loader.load();
            PageGestionSecouristesController controller = loader.getController();
            controller.initialize(admin);
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
