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
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.data.persistence.Secouriste;
import model.data.service.DAOMngt;
import model.data.service.UserMngt;
import model.data.users.UserSecouriste;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Controller for the inscription page
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */

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
    private ImageView backButton;

    @FXML
    private VBox infoContainer;
    @FXML
    private VBox passwordContainer;

    /**
     * Handles the inscription process when the inscription button is clicked.
     * 
     * This method collects user input from various text fields, validates the input,
     * and creates a new user account if all data is valid. It checks for empty fields,
     * password confirmation, and existing user account with the same email. If any 
     * validation fails, it displays an error alert. Upon successful registration, the 
     * method creates a new UserSecouriste and Secouriste, saves them in the database, 
     * and navigates to the Secouriste home page.
     * 
     * @param actionEvent the event triggered by clicking the inscription button
     */
    @FXML
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
        DAOMngt.getSecouristeDAO().create(secouriste);

        user.setIdSecouriste(user.getId());

        user.setSecouriste(secouriste);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PageAccueilSecouriste.fxml"));
        try {
            Parent root = loader.load();
            PageAccueilSecouristeController controller = loader.getController();
            controller.setUser(user);
            Stage stage = (Stage) inscriptionButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Affiche un message d'erreur dans une boite de dialogue.
     *
     * @param title le titre de la boite de dialogue
     * @param message le message a afficher
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Handles the action of clicking the "connexion" hyperlink.
     * 
     * This method loads the connection page FXML and switches the current 
     * scene to the connection interface. It retrieves the current stage 
     * and updates its scene to the newly loaded connection scene. If there 
     * is an error loading the FXML file, it prints the stack trace.
     * 
     * @param actionEvent the event triggered by clicking the "connexion" hyperlink
     */
    @FXML
    public void connexionHandle(ActionEvent actionEvent) {
        try {
            Parent connexionRoot = FXMLLoader.load(getClass().getResource("../resources/fxml/PageConnexion.fxml"));

            Scene connexionScene = new Scene(connexionRoot);

            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            currentStage.setScene(connexionScene);
            currentStage.setTitle("Connexion");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the action of clicking the "suivant" button.
     * 
     * This method shows the information form and hides the password form, the inscription button and the back button.
     * It also shows the suivant button.
     * 
     * @param actionEvent the event triggered by clicking the suivant button
     */
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

        backButton.setVisible(false);
        backButton.setManaged(false);
    }

    /**
     * Handles the action of clicking the "precedent" button.
     * 
     * This method shows the password form and hides the information form, the inscription button and the suivant button.
     * It also shows the precedent button.
     * 
     * @param actionEvent the event triggered by clicking the precedent button
     */
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

        backButton.setVisible(true);
        backButton.setManaged(true);
    }
}

