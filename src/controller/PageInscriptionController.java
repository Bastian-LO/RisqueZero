package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.data.persistence.Secouriste;
import model.data.users.User;
import model.data.users.UserSecouriste;

import java.io.IOException;
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
    private TextField passwordTextField;
    @FXML
    private Button inscriptionButton;
    @FXML
    private TextField confirmationTextField;

    public void inscriptionHandle(ActionEvent actionEvent) {
        String nom = nomTextField.getText();
        String prenom = prenomTextField.getText();
        String mail = mailTextField.getText();
        String dateNaissance = dateNaissanceTextField.getText();
        String telephone = telephoneTextField.getText();
        String password = passwordTextField.getText();
        String adresse = adresseTextField.getText();
        if (nom.isEmpty() || prenom.isEmpty() || mail.isEmpty() || dateNaissance.isEmpty() || telephone.isEmpty()
                || password.isEmpty() || adresse.isEmpty() || confirmationTextField.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }
        if (!password.equals(confirmationTextField.getText())) {
            showAlert("Erreur", "Les mots de passe ne correspondent pas.");
            return;
        }
        UserSecouriste user = new UserSecouriste(mail, password);
        Secouriste secouriste = new Secouriste(user.getId(), nom, prenom, dateNaissance, mail, telephone, adresse, new ArrayList<>(), new HashSet<>());
        user.setIdSecouriste(user.getId());
        user.setSecouriste(secouriste);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PageAccueilSecouristes.fxml"));
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
            Parent root = FXMLLoader.load(getClass().getResource("../resources/fxml/PageConnexion.fxml"));
            Stage stage = (Stage) connexionHyperlink.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

