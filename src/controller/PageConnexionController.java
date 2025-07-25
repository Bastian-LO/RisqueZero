package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.data.service.UserMngt;
import model.data.users.User;
import model.data.users.UserAdmin;
import model.data.users.UserSecouriste;

import static model.data.service.UserMngt.hashPassword;

public class PageConnexionController {

    @FXML
    private TextField IDZoneEcrit;

    @FXML
    private PasswordField MDPZoneEcrit;

    @FXML
    private Button SeConnecterBoutton;

    @FXML
    private Hyperlink LienInscription;

    // Méthode appelée lors du clic sur le bouton de connexion
    @FXML
    public void handleConnexion(ActionEvent event) {
        String login = IDZoneEcrit.getText();
        String motDePasse = MDPZoneEcrit.getText();

        // Validation des champs
        if (login.isEmpty() || motDePasse.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        UserMngt UserManager = new UserMngt();

        boolean connexionReussie = UserManager.verifyPassword(login, motDePasse);

        if (connexionReussie) {
            try {
                User user = UserManager.findUser(login);
                Stage stage = (Stage) SeConnecterBoutton.getScene().getWindow();
                
                if (user.isAdmin()) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PageAccueilAdmin.fxml"));
                    Parent root = loader.load();
                    PageAccueilAdminController controller = loader.getController();
                    controller.setUser((UserAdmin) user);
                    stage.setScene(new Scene(root));
                } else {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PageAccueilSecouriste.fxml"));
                    Parent root = loader.load();
                    PageAccueilSecouristeController controller = loader.getController();
                    controller.setUser((UserSecouriste) user);
                    stage.setScene(new Scene(root));
                }
                
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible de charger l'interface: " + e.getMessage());
            }
        } else {
            showAlert("Erreur de connexion", "Identifiant ou mot de passe incorrect.");
    }
    }


    // Méthode utilitaire pour afficher des alertes
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode appelée lors du clic sur le lien d'inscription
    @FXML
    public void LienInscriptionHandle(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../resources/fxml/PageInscription.fxml"));
            Stage stage = (Stage) LienInscription.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}