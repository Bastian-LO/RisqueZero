package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.data.persistence.Competence;
import model.data.persistence.DPS;
import model.data.service.DAOMngt;
import model.data.users.UserAdmin;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PageModifDPSController {

    public TextField heureDebutTextField;
    public TextField minuteDebutTextField;
    public TextField sportTextField;
    public TextField siteTextField;
    public Label dateLabel;
    public TextField heureFinTextField;
    public TextField minuteFinTextField;
    // Déclaration des éléments FXML
    @FXML
    private Button BouttonAccueil;
    @FXML
    private Button BouttonAffecter;

    @FXML
    private Button BouttonEnregistrer;
    @FXML
    private Button BouttonSupprimer;

    @FXML
    private ComboBox<String> ComboBoxCompetences;
    @FXML
    private ListView<String> ListeDeCompetences;
    @FXML
    private DatePicker Calendrier;

    // Champs pour stocker les données
    private DPS dpsEnCours;

    private UserAdmin user;


    // Initialisation du contrôleur
    @FXML
    public void initialize(UserAdmin user, DPS dps) {
        this.dpsEnCours = dps;
        chargerDonneesDPS();

        // Peuplement des ComboBox avec des données
        sportTextField.setText(dps.getSport().getNom());
        siteTextField.setText(dps.getLieu().getNom());
        ComboBoxCompetences.getItems().addAll("PSC1", "PSE1", "PSE2", "DEA", "BNSSA");

        if (dpsEnCours != null && dpsEnCours.getDateEvt() != null) {
            Calendrier.setValue(dpsEnCours.getDateEvt().toLocalDate());

            heureDebutTextField.setText(dpsEnCours.getHoraireDepart()[0] + "");
            minuteDebutTextField.setText(dpsEnCours.getHoraireDepart()[0] + "");
            heureFinTextField.setText(dpsEnCours.getHoraireFin()[0] + "");
            minuteFinTextField.setText(dpsEnCours.getHoraireFin()[1] + "");
        } else {
            Calendrier.setValue(LocalDate.now());
        }

        this.user = user;
    }


    private void chargerDonneesDPS() {
        if (dpsEnCours != null) {
            sportTextField.setText(this.dpsEnCours.getSport().getNom());
            siteTextField.setText(this.dpsEnCours.getLieu().getNom());
            Calendrier.setValue(dpsEnCours.getDateEvt().toLocalDate());

            List<Competence> competences = DAOMngt.getCompetenceDAO().findAll();
            List<Competence> competencesManquantes = new ArrayList<>();
            for (Competence competence : competences) {
                if (!dpsEnCours.getCompetences().contains(competence)) {
                    competencesManquantes.add(competence);
                }
                List<Competence> sublist = competencesManquantes.subList(0, Math.min(5, competences.size()));
                for (Competence comp : sublist) {
                    ListeDeCompetences.getItems().add(comp.getIntitule());
                }
            }
        }
    }

    // Handlers définis dans le FXML
    @FXML
    public void BouttonAccueilHandle(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Retour");
        alert.setHeaderText("SAUVEGARDE");
        alert.setContentText("Avez vous bien enregistré ?");

        if (alert.showAndWait().isPresent() && alert.showAndWait().get() == ButtonType.OK) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PageDPS.fxml"));
            try {
                Parent root = loader.load();
                PageDPSController controller = loader.getController();
                controller.initialize(user);
                Stage stage = (Stage) BouttonAccueil.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (
                    IOException e) {
                e.printStackTrace();
            }

        }
    }

    @FXML
    public void BouttonAffecterHandle(ActionEvent event) {
        System.out.println("Affectation des secouristes");
        // TODO LES PUTAINS DE GRAPHES DE CON
    }

    @FXML
    public void BouttonEnregistrerHandle(ActionEvent event) {
        //TODO ENREGISTRER DANS LA BDD TOUTE LES INFOS DE LA PAGE SSI ELLES SONT VALIDES
    }

    @FXML
    public void BouttonSupprimerHandle(ActionEvent event) {
        System.out.println("Suppression du DPS");
        // Confirmation et suppression
        confirmerSuppression();
    }

    @FXML
    public void sportHandle(ActionEvent event) {
        // jsp si utile
    }

    @FXML
    public void siteHandle(ActionEvent event) {
        //jsp non plus
    }

    @FXML
    public void ComboBoxCompetencesHandle(ActionEvent event) {
        String competenceSelectionnee = ComboBoxCompetences.getValue();
        this.dpsEnCours.getCompetences().add(DAOMngt.getCompetenceDAO().findByIntitule(competenceSelectionnee));

        if (competenceSelectionnee != null && !ListeDeCompetences.getItems().contains(competenceSelectionnee)) {
            ListeDeCompetences.getItems().add(competenceSelectionnee);
        }
    }


    private void confirmerSuppression() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le dispositif ?");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer définitivement ce DPS ?");

        if (alert.showAndWait().isPresent() && alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("DPS supprimé: " + dpsEnCours.getId());
            DAOMngt.getDPSDAO().delete(dpsEnCours);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PageAccueilAdmin.fxml"));
            try {
                Parent root = loader.load();
                PageDPSController controller = loader.getController();
                controller.initialize(user);
                Stage stage = (Stage) BouttonAccueil.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (
                    IOException e) {
                e.printStackTrace();
            }
        }
    }



    private void showAlert(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void calendrierHandle(ActionEvent actionEvent) {
        if (Calendrier.getValue() != null) {

        }
    }
    public void heureDebutTextFieldHandle(ActionEvent actionEvent) {

    }
    public void minuteDebutTextFieldHandle(ActionEvent actionEvent) {
    }

    public void heureFinTextFieldHandle(ActionEvent actionEvent) {
    }

    public void minuteFinTextFieldHandle(ActionEvent actionEvent) {
    }
}