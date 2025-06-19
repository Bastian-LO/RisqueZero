package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.data.persistence.Secouriste;
import model.data.service.DAOMngt;
import model.data.users.UserAdmin;

/**
 * Controller for the modif secouriste page
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */

public class PageModifSecouristeController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField dateNaissanceField;
    @FXML private TextField emailField;
    @FXML private TextField telField;
    @FXML private TextField adresseField;
    @FXML private ListView<String> competencesList;
    @FXML private ComboBox<String> competencesComboBox;
    @FXML private Button enregistrerButton;
    @FXML private Button supprimerButton;
    @FXML private Button deconnexionButton;

    private Secouriste secouriste;
    private Stage dialogStage;
    private UserAdmin user;

    /**
     * Sets the current user to the given user
     * @param user the current user
     */
    public void setUser(UserAdmin user) {
        this.user = user;
    }


/**
 * Initializes the modification page with the given secouriste and dialog stage.
 * This method sets the secouriste and dialog stage attributes and then populates
 * the form fields with the secouriste's information. It also loads the available
 * competences into the relevant UI components.
 *
 * @param secouriste the secouriste whose information is to be displayed and modified
 * @param dialogStage the stage for the current dialog window
 */
    public void initialize(Secouriste secouriste, Stage dialogStage) {
        this.secouriste = secouriste;
        this.dialogStage = dialogStage;
        remplirChamps();
        chargerCompetences();
    }


    /**
     * Sets the current secouriste to the given secouriste.
     * This method sets the secouriste attribute and then populates
     * the form fields with the secouriste's information. It also loads
     * the available competences into the relevant UI components.
     *
     * @param secouriste the secouriste whose information is to be displayed and modified
     */
    public void setSecouriste(Secouriste secouriste) {
        this.secouriste = secouriste;
        remplirChamps();
        chargerCompetences();
    }

    /**
     * Remplit les champs du formulaire avec les informations du secouriste
     */
    private void remplirChamps() {
        nomField.setText(secouriste.getNom());
        prenomField.setText(secouriste.getPrenom());
        dateNaissanceField.setText(secouriste.getDateNaissance());
        emailField.setText(secouriste.getEmail());
        telField.setText(secouriste.getTel());
        adresseField.setText(secouriste.getAdresse());
    }

/**
 * Loads the competences of the current secouriste into the UI component.
 * Clears the existing items in the competencesList and repopulates it
 * with the names of the competences associated with the secouriste.
 */

    private void chargerCompetences() {
        competencesList.getItems().clear();
        competencesList.getItems().addAll(secouriste.getCompetencesIntitules());

    }

    /**
     * Handles the button click event when the user wants to save
     * changes made to the secouriste's information.
     *
     * If the information entered is valid, updates the secouriste
     * object and saves the changes to the database. Then closes
     * the modal window.
     */
    @FXML
    public void handleEnregistrer() {
        if (estSaisieValide()) {
            mettreAJourSecouriste();
            DAOMngt.getSecouristeDAO().update(secouriste);
            dialogStage.close();
        }
    }

    /**
     * Handles the button click event when the user wants to delete
     * the current secouriste.
     *
     * If the user confirms, deletes the secouriste from the database
     * and closes the modal window.
     */
    @FXML
    public void handleSupprimer() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le secouriste");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce secouriste ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                DAOMngt.getSecouristeDAO().delete(secouriste);
                dialogStage.close();
            }
        });
    }

    /**
     * Handles the button click event when the user wants to log out.
     *
     * Closes the current window and opens the login window.
     */
    @FXML
    public void handleDeconnexion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PageConnexion.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) deconnexionButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/**
 * Updates the current secouriste's information using the data entered in the form fields.
 * 
 * This method retrieves the text from the input fields for the secouriste's name, 
 * first name, date of birth, email, telephone, and address, and updates the 
 * corresponding attributes of the secouriste object.
 */
    private void mettreAJourSecouriste() {
        secouriste.setNom(nomField.getText());
        secouriste.setPrenom(prenomField.getText());
        secouriste.setDateNaissance(dateNaissanceField.getText());
        secouriste.setEmail(emailField.getText());
        secouriste.setTel(telField.getText());
        secouriste.setAdresse(adresseField.getText());
    }

    /**
     * Checks if the input fields for the secouriste's information are valid.
     * 
     * This method checks if the input fields for the secouriste's name, first name, date of birth, email, telephone, and address are valid according to the following rules:
     * - name and first name must not be empty
     * - date of birth must be in the format JJ/MM/AAAA
     * - email must be in the format XXX@XXX.XXX
     * - telephone must be in the format 0123456789
     * - address must not be empty
     * 
     * If any of the fields are invalid, an alert box is displayed listing the errors.
     * If all fields are valid, the method returns true.
     * 
     * @return true if all input fields are valid, false otherwise
     */
    private boolean estSaisieValide() {
        String errorMessage = "";

        if (nomField.getText() == null || nomField.getText().isEmpty()) {
            errorMessage += "Nom invalide!\n";
        }
        if (prenomField.getText() == null || prenomField.getText().isEmpty()) {
            errorMessage += "Prénom invalide!\n";
        }
        if (dateNaissanceField.getText() == null || !dateNaissanceField.getText().matches("^\\d{2}/\\d{2}/\\d{4}$")) {
            errorMessage += "Date de naissance invalide (format JJ/MM/AAAA attendu)!\n";
        }
        if (emailField.getText() == null || !emailField.getText().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            errorMessage += "Email invalide!\n";
        }
        if (telField.getText() == null || !telField.getText().matches("^0\\d{9}$")) {
            errorMessage += "Téléphone invalide (format 0123456789 attendu)!\n";
        }
        if (adresseField.getText() == null || adresseField.getText().isEmpty()) {
            errorMessage += "Adresse invalide!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText("Veuillez corriger les erreurs suivantes");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}