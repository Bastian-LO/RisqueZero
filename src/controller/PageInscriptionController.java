package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.data.persistence.Secouriste;
import model.data.service.UserMngt;
import model.data.users.User;
import model.data.users.UserSecouriste;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashSet;

public class PageInscriptionController {

    @FXML
    public Hyperlink connexionHyperlink;
    @FXML
    private TextField nomTextField;
    @FXML
    private TextField prenomTextField;
    @FXML
    private TextField mailTextField;
    @FXML
    private TextField dateNaissanceTextField;
    @FXML
    private TextField telephoneTextField;
    @FXML
    private TextField adresseTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Button inscriptionButton;
    @FXML
    private Button suivantButton;
    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private VBox infoContainer;
    @FXML
    private VBox passwordContainer;

    public void inscriptionHandle(ActionEvent actionEvent) {
        String nom = nomTextField.getText();
        String prenom = prenomTextField.getText();
        String mail = mailTextField.getText();
        String dateNaissance = dateNaissanceTextField.getText();
        String telephone = telephoneTextField.getText();
        String password = passwordPasswordField.getText();
        String adresse = adresseTextField.getText();
        if (nom.isEmpty() || prenom.isEmpty() || mail.isEmpty() || dateNaissance.isEmpty() || telephone.isEmpty()
                || password.isEmpty() || adresse.isEmpty() || confirmPasswordField.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }
        if (!password.equals(confirmPasswordField.getText())) {
            showAlert("Erreur", "Les mots de passe ne correspondent pas.");
            return;
        }
        UserMngt userMngt = new UserMngt();
        UserSecouriste user1 = (UserSecouriste) userMngt.findUser(mail);
        if (user1 != null) {
            showAlert("Erreur", "Un compte existe deja avec cette adresse mail");
            return;
        }

        UserSecouriste user = new UserSecouriste(mail, password);

        Secouriste secouriste = new Secouriste(user.getId(), nom, prenom, dateNaissance, mail, telephone, adresse, new ArrayList<>(), new HashSet<>());
        user.setIdSecouriste(user.getId());
        user.setSecouriste(secouriste);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PageAccueilSecouriste.fxml"));
        try {
            Parent root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PageAccueilSecouristeController controller = loader.getController();
        controller.setUser(user);
        // Assuming you have a setUser method in your PageAccueilSecouristeController
    }


    // Méthode utilitaire pour afficher des alertes
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void connexionHandle(ActionEvent actionEvent) {
        try {
            // Chargement de la deuxième interface depuis second.fxml
            Parent connexionRoot = FXMLLoader.load(getClass().getResource("../resources/fxml/PageConnexion.fxml"));

            // Création de la nouvelle scène
            Scene connexionScene = new Scene(connexionRoot);

            // Récupération de la fenêtre actuelle
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Changement de la scène
            currentStage.setScene(connexionScene);
            currentStage.setTitle("Connexion");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Navigation entre étapes
    @FXML
    public void goToInformation() {
        infoContainer.setVisible(true);
        infoContainer.setManaged(true);

        passwordContainer.setVisible(false);
        passwordContainer.setManaged(false);

        inscriptionButton.setVisible(false);
        inscriptionButton.setManaged(false);
        suivantButton.setVisible(true);
        suivantButton.setManaged(true);
    }

    @FXML
    public void goToPassword() {
        infoContainer.setVisible(false);
        infoContainer.setManaged(false);

        passwordContainer.setVisible(true);
        passwordContainer.setManaged(true);

        inscriptionButton.setVisible(true);
        inscriptionButton.setManaged(true);
        suivantButton.setVisible(false);
        suivantButton.setManaged(false);
    }
}

