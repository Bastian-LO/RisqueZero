package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.data.persistence.Secouriste;
import model.data.service.DAOMngt;
import model.data.users.User;

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
    private User user;

    public void initialize(Secouriste secouriste, Stage dialogStage) {
        this.secouriste = secouriste;
        this.dialogStage = dialogStage;
        remplirChamps();
        chargerCompetences();
    }

    private void remplirChamps() {
        nomField.setText(secouriste.getNom());
        prenomField.setText(secouriste.getPrenom());
        dateNaissanceField.setText(secouriste.getDateNaissance());
        emailField.setText(secouriste.getEmail());
        telField.setText(secouriste.getTel());
        adresseField.setText(secouriste.getAdresse());
    }

    private void chargerCompetences() {
        competencesList.getItems().addAll(secouriste.getCompetencesIntitules());
        // Ici vous pourriez charger les compétences disponibles dans le ComboBox
    }

    @FXML
    private void handleEnregistrer() {
        if (estSaisieValide()) {
            mettreAJourSecouriste();
            DAOMngt.getSecouristeDAO().update(secouriste);
            dialogStage.close();
        }
    }

    @FXML
    private void handleSupprimer() {
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

    @FXML
    private void handleDeconnexion() {
        // Implémentez la déconnexion ici
        dialogStage.close();
    }

    private void mettreAJourSecouriste() {
        secouriste.setNom(nomField.getText());
        secouriste.setPrenom(prenomField.getText());
        secouriste.setDateNaissance(dateNaissanceField.getText());
        secouriste.setEmail(emailField.getText());
        secouriste.setTel(telField.getText());
        secouriste.setAdresse(adresseField.getText());
    }

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

    public void setSecouriste(Secouriste secouriste) {
        this.secouriste = secouriste;
        remplirChamps();
        chargerCompetences();
    }
}