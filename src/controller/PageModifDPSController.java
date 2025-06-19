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
import model.data.persistence.Journee;
import model.data.service.DAOMngt;
import model.data.users.UserAdmin;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
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


    @FXML
    public void initialize(UserAdmin user, DPS dps) {
        this.dpsEnCours = dps;
        this.user = user;

        // Initialize UI components
        sportTextField.setText(dps.getSport().getNom());
        siteTextField.setText(dps.getLieu().getNom());
        ComboBoxCompetences.getItems().addAll("PSC1", "PSE1", "PSE2", "DEA", "BNSSA");

        // Set date and time fields from DPS
        if (dpsEnCours != null && dpsEnCours.getDateEvt() != null) {
            Calendrier.setValue(dpsEnCours.getDateEvt().toLocalDate());

            // Fixed array indices for time fields
            if (dpsEnCours.getHoraireDepart() != null && dpsEnCours.getHoraireDepart().length >= 2) {
                heureDebutTextField.setText(String.valueOf(dpsEnCours.getHoraireDepart()[0]));
                minuteDebutTextField.setText(String.valueOf(dpsEnCours.getHoraireDepart()[1]));
            }
            if (dpsEnCours.getHoraireFin() != null && dpsEnCours.getHoraireFin().length >= 2) {
                heureFinTextField.setText(String.valueOf(dpsEnCours.getHoraireFin()[0]));
                minuteFinTextField.setText(String.valueOf(dpsEnCours.getHoraireFin()[1]));
            }
        } else {
            Calendrier.setValue(LocalDate.now());
        }

        chargerDonneesDPS();
    }

    private void chargerDonneesDPS() {
        if (dpsEnCours == null) return;

        // Clear existing items
        ListeDeCompetences.getItems().clear();

        // Load available competences
        List<Competence> allCompetences = DAOMngt.getCompetenceDAO().findAll();
        List<Competence> missingCompetences = new ArrayList<>();

        for (Competence competence : allCompetences) {
            if (!dpsEnCours.getCompetences().contains(competence)) {
                missingCompetences.add(competence);
            }
        }

        int maxCompetencesToShow = 5;
        for (int i = 0; i < Math.min(missingCompetences.size(), maxCompetencesToShow); i++) {
            Competence comp = missingCompetences.get(i);
            ListeDeCompetences.getItems().add(comp.getIntitule());
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PageAffectation.fxml"));
        try {
            Parent root = loader.load();
            PageAffectationController controller = loader.getController();
            controller.initialize(user,dpsEnCours);
            Stage stage = (Stage) BouttonAffecter.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void BouttonEnregistrerHandle(ActionEvent event) {
        // Validation des champs obligatoires
        if (!validerChampsObligatoires()) {
            return;
        }

        // Validation des formats de temps
        if (!validerTemps()) {
            return;
        }

        // Récupération des valeurs modifiées
        String sport = sportTextField.getText();
        String site = siteTextField.getText();
        LocalDate date = Calendrier.getValue();

        // Conversion des horaires
        int[] horaireDebut = {
                Integer.parseInt(heureDebutTextField.getText()),
                Integer.parseInt(minuteDebutTextField.getText())
        };

        int[] horaireFin = {
                Integer.parseInt(heureFinTextField.getText()),
                Integer.parseInt(minuteFinTextField.getText())
        };

        // Mise à jour du DPS
        dpsEnCours.getSport().setNom(sport);
        dpsEnCours.getLieu().setNom(site);
        dpsEnCours.setdateEvt(new Journee(date));
        dpsEnCours.setHoraireDepart(horaireDebut);
        dpsEnCours.setHoraireFin(horaireFin);

        try {
            DAOMngt.getDPSDAO().update(dpsEnCours);
        } catch (Exception e) {
            e.printStackTrace();
        }
        showAlert("Succès", "Modifications enregistrées avec succès", Alert.AlertType.INFORMATION);
    }

    // Méthode de validation des champs obligatoires
    private boolean validerChampsObligatoires() {
        if (sportTextField.getText().isEmpty() ||
                siteTextField.getText().isEmpty() ||
                Calendrier.getValue() == null) {

            showAlert("Champs manquants",
                    "Veuillez remplir tous les champs obligatoires",
                    Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    // Méthode de validation des formats de temps
    private boolean validerTemps() {
        boolean valid = true;

        if (invalideFormatTemps(heureDebutTextField, 0, 23)) valid = false;
        if (invalideFormatTemps(minuteDebutTextField, 0, 59)) valid = false;
        if (invalideFormatTemps(heureFinTextField, 0, 23)) valid = false;
        if (invalideFormatTemps(minuteFinTextField, 0, 59)) valid = false;

        return valid;
    }

    // Méthode utilitaire pour la validation des champs temps
    private boolean invalideFormatTemps(TextField field, int min, int max) {
        try {
            int value = Integer.parseInt(field.getText());
            if (value < min || value > max) {
                field.setStyle("-fx-text-fill: red;");
                showAlert("Valeur incorrecte",
                        "La valeur doit être entre " + min + " et " + max,
                        Alert.AlertType.WARNING);
                return true;
            }
            field.setStyle("-fx-text-fill: black;");
            return false;
        } catch (NumberFormatException e) {
            field.setStyle("-fx-text-fill: red;");
            showAlert("Format incorrect", "Veuillez entrer un nombre valide", Alert.AlertType.WARNING);
            return true;
        }
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
    public void heureDebutTextFieldHandle(ActionEvent actionEvent) {
        validateTimeField(heureDebutTextField, 0, 23);
    }

    public void minuteDebutTextFieldHandle(ActionEvent actionEvent) {
        validateTimeField(minuteDebutTextField, 0, 59);
    }

    public void heureFinTextFieldHandle(ActionEvent actionEvent) {
        validateTimeField(heureFinTextField, 0, 23);
    }

    public void minuteFinTextFieldHandle(ActionEvent actionEvent) {
        validateTimeField(minuteFinTextField, 0, 59);
    }

    private void validateTimeField(TextField field, int min, int max) {
        try {
            int value = Integer.parseInt(field.getText());
            if (value < min || value > max) {
                field.setStyle("-fx-text-fill: red;");
                showAlert("Valeur incorrecte",
                        "La valeur doit être entre " + min + " et " + max,
                        Alert.AlertType.WARNING);
            } else {
                field.setStyle("-fx-text-fill: black;");
            }
        } catch (NumberFormatException e) {
            field.setStyle("-fx-text-fill: red;");
            showAlert("Format incorrect", "Veuillez entrer un nombre valide", Alert.AlertType.WARNING);
        }
    }

    public void calendrierHandle(ActionEvent actionEvent) {
    }
}
