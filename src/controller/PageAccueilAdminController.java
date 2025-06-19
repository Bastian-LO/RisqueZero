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

/**
 * Controller for the admin home page.
 * @author Emile Thevenin
 */
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

    /**
     * Sets the admin user and updates the UI accordingly.
     * @param admin the admin user to set
     */
    public void setUser(UserAdmin admin){
        this.admin = admin;
        updateUI();
    }

    /**
     * Updates the UI by refreshing the profile labels and DPS information.
     */
    private void updateUI() {
        updateProfileLabels();
        updateDPS();
    }

    /**
     * Updates the DPS label by retrieving the next upcoming DPS event.
     */
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
        for (DPS dps1 : dps) {
            if (dps1.getDateEvt().isBefore(nextDPS.getDateEvt())) {
                nextDPS = dps1;
            }
        }

        idEvenementLabel.setText(String.valueOf(nextDPS.getId()));
        sportLabel.setText(nextDPS.getSport().getNom());
        siteLabel.setText(nextDPS.getLieu().getNom());
        dateLabel.setText(nextDPS.getDateEvt().toString());
    }

    /**
     * Updates the profile labels with the admin's information.
     */
    private void updateProfileLabels() {
        idProfilLabel.setText(String.valueOf(admin.getId()));
        mailLabel.setText(admin.getLogin());
    }

    /**
     * Logs out the admin and redirects to the login page.
     * @param actionEvent the action event that triggered this method
     */
    public void deconnexionPage(ActionEvent actionEvent) {
        try {
            Parent connexionRoot = FXMLLoader.load(getClass().getResource("../resources/fxml/PageConnexion.fxml"));
            Scene connexionScene = new Scene(connexionRoot);
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.setScene(connexionScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the secouristes management page.
     * @param actionEvent the action event that triggered this method
     */
    public void secouristePage(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PageGestionSecouristes.fxml"));
            Parent secouristeRoot = loader.load();
            PageGestionSecouristesController controller = loader.getController();
            controller.initialize(admin);
            Scene secouristeScene = new Scene(secouristeRoot);
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.setScene(secouristeScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the DPS page.
     * @param actionEvent the action event that triggered this method
     */
    public void dpsPage(ActionEvent actionEvent) {
        try {
            Parent secouristeRoot = FXMLLoader.load(getClass().getResource("../resources/fxml/PageDPS.fxml"));
            Scene secouristeScene = new Scene(secouristeRoot);
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.setScene(secouristeScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}