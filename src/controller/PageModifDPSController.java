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


/**
 * Controller for the modif DPS page
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */
public class PageModifDPSController {

    public TextField heureDebutTextField;
    public TextField minuteDebutTextField;
    public TextField sportTextField;
    public TextField siteTextField;
    public Label dateLabel;
    public TextField heureFinTextField;
    public TextField minuteFinTextField;
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

    private DPS dpsEnCours;

    private UserAdmin user;


    /**
     * Initializes the controller. Called after the FXML file has been loaded
     * @param user the current user
     * @param dps the DPS to modify
     */
    @FXML
    public void initialize(UserAdmin user, DPS dps) {
        this.dpsEnCours = dps;
        this.user = user;

        sportTextField.setText(dps.getSport().getNom());
        siteTextField.setText(dps.getLieu().getNom());
        ComboBoxCompetences.getItems().addAll("PSC1", "PSE1", "PSE2", "DEA", "BNSSA");

        if (dpsEnCours != null && dpsEnCours.getDateEvt() != null) {
            Calendrier.setValue(dpsEnCours.getDateEvt().toLocalDate());

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

    /**
     * Charges les données pour le DPS en cours
     * Affiche les compétences manquantes pour le DPS
     * et ajoute un maximum de 5 items dans la liste
     */
    private void chargerDonneesDPS() {
        if (dpsEnCours == null) return;

        ListeDeCompetences.getItems().clear();

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


    /**
     * Handles the action of clicking the "Accueil" button.
     * 
     * This method prompts the user with a confirmation dialog to ensure
     * they have saved their changes. If the user confirms, it loads the
     * DPS page interface, passing the current user data to the controller,
     * and updates the stage scene to the newly loaded interface. It catches
     * and prints any IOExceptions that occur during this process.
     * 
     * @param event the ActionEvent triggered by clicking the "Accueil" button
     */
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

    /**
     * Handles the action of clicking the "Affecter" button.
     * 
     * This method loads the affectation page interface, passing the current user data and the
     * current DPS data to the controller, and updates the stage scene to the newly loaded interface.
     * It catches and prints any IOExceptions that occur during this process.
     * 
     * @param event the ActionEvent triggered by clicking the "Affecter" button
     */
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

    /**
     * Handles the action of clicking the "Enregistrer" button.
     * 
     * This method first validates the required fields and the time entries.
     * If validations are successful, it retrieves the data from the input fields,
     * updates the current DPS object with the new values, and saves the changes
     * to the database. It catches any exceptions that occur during the update
     * process and shows a success alert upon successful completion.
     * 
     * @param event the ActionEvent triggered by clicking the "Enregistrer" button
     */
    @FXML
    public void BouttonEnregistrerHandle(ActionEvent event) {
        if (!validerChampsObligatoires()) {
            return;
        }

        if (!validerTemps()) {
            return;
        }

        String sport = sportTextField.getText();
        String site = siteTextField.getText();
        LocalDate date = Calendrier.getValue();

        int[] horaireDebut = {
                Integer.parseInt(heureDebutTextField.getText()),
                Integer.parseInt(minuteDebutTextField.getText())
        };

        int[] horaireFin = {
                Integer.parseInt(heureFinTextField.getText()),
                Integer.parseInt(minuteFinTextField.getText())
        };

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

    /**
     * Valide que les champs obligatoires sont remplis
     * @return true si les champs sont remplis, false sinon
     */
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

    /**
     * Valide que les champs temps sont valides
     * @return true si les champs sont valides, false sinon
     */
    private boolean validerTemps() {
        boolean valid = true;

        if (invalideFormatTemps(heureDebutTextField, 0, 23)) valid = false;
        if (invalideFormatTemps(minuteDebutTextField, 0, 59)) valid = false;
        if (invalideFormatTemps(heureFinTextField, 0, 23)) valid = false;
        if (invalideFormatTemps(minuteFinTextField, 0, 59)) valid = false;

        return valid;
    }

/**
 * Checks if the value of a TextField is within a specified range.
 * 
 * This method attempts to parse the text of a given TextField to an integer
 * and verifies if it falls within the specified minimum and maximum values.
 * If the value is out of range or cannot be parsed, the text color is set
 * to red and an alert is shown to the user.
 * 
 * @param field The TextField whose value is to be validated.
 * @param min The minimum allowable value.
 * @param max The maximum allowable value.
 * @return true if the value is invalid (out of range or non-numeric), false otherwise.
 */
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

/**
 * Handles the action of clicking the "Supprimer" button.
 * 
 * This method logs the deletion action to the console and calls a 
 * confirmation method to verify if the user wants to proceed with 
 * deleting the current DPS. If confirmed, the DPS is removed from 
 * the database and the interface is updated accordingly.
 * 
 * @param event the ActionEvent triggered by clicking the "Supprimer" button
 */
    @FXML
    public void BouttonSupprimerHandle(ActionEvent event) {
        System.out.println("Suppression du DPS");
        confirmerSuppression();
    }

/**
 * Handles the action triggered by an event related to the sport input field.
 * 
 * This method is currently a placeholder and does not perform any specific
 * action. It can be implemented to handle actions such as validating the
 * sport input or updating the DPS object with sport-related data.
 * 
 * @param event the ActionEvent triggered by interacting with the sport input field
 */
    @FXML
    public void sportHandle(ActionEvent event) {
    }

    /**
     * Handles the action triggered by an event related to the site input field.
     * 
     * This method is currently a placeholder and does not perform any specific
     * action. It can be implemented to handle actions such as validating the
     * site input or updating the DPS object with site-related data.
     * 
     * @param event the ActionEvent triggered by interacting with the site input field
     */
    @FXML
    public void siteHandle(ActionEvent event) {
    }

/**
 * Handles the selection of a competence from the ComboBox.
 * 
 * This method retrieves the selected competence from the ComboBox
 * and adds it to the current DPS's competence list. If the competence
 * is not already present in the displayed list of competences, it is
 * also added to the list view for display purposes.
 * 
 * @param event the ActionEvent triggered by selecting an item from the ComboBox
 */
    @FXML
    public void ComboBoxCompetencesHandle(ActionEvent event) {
        String competenceSelectionnee = ComboBoxCompetences.getValue();
        this.dpsEnCours.getCompetences().add(DAOMngt.getCompetenceDAO().findByIntitule(competenceSelectionnee));

        if (competenceSelectionnee != null && !ListeDeCompetences.getItems().contains(competenceSelectionnee)) {
            ListeDeCompetences.getItems().add(competenceSelectionnee);
        }
    }


    /**
     * Shows a confirmation dialog to the user to ensure they want to
     * permanently delete the current DPS. If the user confirms, the
     * DPS is removed from the database and the interface is updated
     * accordingly.
     */
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



    /**
     * Affiche un message d'erreur dans une boite de dialogue.
     * 
     * @param titre le titre de la boite de dialogue
     * @param message le message a afficher
     * @param type le type d'alerte a afficher (INFORMATION, WARNING, ERROR)
     */
    private void showAlert(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    /**
     * Valide le champ de texte heureDebutTextField en vérifiant si son contenu
     * est un entier compris entre 0 et 23. Si la validation échoue, affiche
     * un message d'erreur.
     * 
     * @param actionEvent l'ActionEvent déclenché par le changement de valeur
     *                    du champ de texte
     */
    public void heureDebutTextFieldHandle(ActionEvent actionEvent) {
        validateTimeField(heureDebutTextField, 0, 23);
    }

/**
 * Handles the action triggered by an event related to the minute debut input field.
 * 
 * This method validates the input in the minuteDebutTextField to ensure it is an integer
 * between 0 and 59. If the input is invalid, it displays an error alert.
 * 
 * @param actionEvent the ActionEvent triggered by changes in the minute debut input field
 */
    public void minuteDebutTextFieldHandle(ActionEvent actionEvent) {
        validateTimeField(minuteDebutTextField, 0, 59);
    }

    /**
     * Handles the action triggered by an event related to the heure fin input field.
     * 
     * This method validates the input in the heureFinTextField to ensure it is an integer
     * between 0 and 23. If the input is invalid, it displays an error alert.
     * 
     * @param actionEvent the ActionEvent triggered by changes in the heure fin input field
     */
    public void heureFinTextFieldHandle(ActionEvent actionEvent) {
        validateTimeField(heureFinTextField, 0, 23);
    }

    /**
     * Handles the action triggered by an event related to the minute fin input field.
     * 
     * This method validates the input in the minuteFinTextField to ensure it is an integer
     * between 0 and 59. If the input is invalid, it displays an error alert.
     * 
     * @param actionEvent the ActionEvent triggered by changes in the minute fin input field
     */
    public void minuteFinTextFieldHandle(ActionEvent actionEvent) {
        validateTimeField(minuteFinTextField, 0, 59);
    }

    /**
     * Validates the input in the given TextField to ensure it is an integer
     * between min and max. If the input is invalid, it displays an error alert.
     * 
     * @param field the TextField to validate
     * @param min the minimum value of the range
     * @param max the maximum value of the range
     */
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
