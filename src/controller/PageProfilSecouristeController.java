package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.data.persistence.Competence;
import model.data.persistence.Dispos;
import model.data.persistence.Secouriste;
import model.data.users.UserSecouriste;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the profil secouriste page
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */

public class PageProfilSecouristeController {
    @FXML
    public Button BouttonAccueil;
    @FXML
    public Label Dispo1;
    @FXML
    public Label Dispo2;
    @FXML
    public Label Dispo3;
    @FXML
    public Label Dispo4;
    @FXML
    public Label Dispo5;
    @FXML
    public Label Dispo6;
    @FXML
    public Button BouttonAjouter;
    @FXML
    public Label Info1;
    @FXML
    public Label Info2;
    @FXML
    public Label Info3;
    @FXML
    public Label Info4;
    @FXML
    public Label Info5;
    @FXML
    public Label Info6;
    @FXML
    public Label Comp1;
    @FXML
    public Label Comp2;
    @FXML
    public Label Comp3;
    @FXML
    public Label Comp4;
    @FXML
    public Label Comp5;
    @FXML
    public Label Comp6;

    private UserSecouriste user;
    private Secouriste secouriste;

    /**
     * Sets the current user to the given user
     * and updates the UI with the secouriste's information
     * @param user the current user
     */
    public void setUser(UserSecouriste user) {
        this.user = user;
        this.secouriste = user.getSecouriste();
        updateUI();
    }



    /**
     * Updates the UI with the secouriste's information.
     * <p>
     * This method updates the labels in the UI with the secouriste's personal information
     * (ID, name, birthdate, email, phone number, address), as well as their disponibilities
     * and competences.
     * <p>
     * This method is called when the user is set, and is also called by the user when
     * they want to refresh the information displayed on the page.
     */
    private void updateUI() {
        Info1.setText("- ID : " + this.secouriste.getId());
        Info2.setText("- Nom et Prénom : " + secouriste.getNom() + " " + secouriste.getPrenom());
        Info3.setText("- Date de naissance : " + secouriste.getDateNaissance());
        Info4.setText("- Adresse mail : " + secouriste.getEmail());
        Info5.setText("- Téléphone : " + secouriste.getTel());
        Info6.setText("- Adresse : " + secouriste.getAdresse());

        updateDisponibilites();

        updateCompetences();
    }

    /**
     * Updates the disponibilities' labels with the secouriste's disponibilities.
     * <p>
     * This method clears all the labels and then sets the text of the first 6 labels to the
     * corresponding disponibilities of the secouriste. If the secouriste has less than 6
     * disponibilities, the remaining labels are left empty.
     */
    private void updateDisponibilites() {
        List<Dispos> dispos = new ArrayList<>(secouriste.getDisponibilites());

        List<Label> dispoLabels = List.of(Dispo1, Dispo2, Dispo3, Dispo4, Dispo5, Dispo6);
        dispoLabels.forEach(label -> label.setText("        "));

        int max = Math.min(dispos.size(), 6);
        for (int i = 0; i < max; i++) {
            Dispos d = dispos.get(i);
            String text = d.toString();
            dispoLabels.get(i).setText(text);
        }
    }

    /**
     * Updates the competences' labels with the secouriste's competences.
     * <p>
     * This method clears all the labels and then sets the text of the first 6 labels to the
     * corresponding competences of the secouriste. If the secouriste has less than 6
     * competences, the remaining labels are left empty.
     */
    private void updateCompetences() {
        List<Competence> competences = new ArrayList<>(secouriste.getCompetences());

        List<Label> compLabels = List.of(Comp1, Comp2, Comp3, Comp4, Comp5, Comp6);
        compLabels.forEach(label -> label.setText("        "));

        int max = Math.min(competences.size(), 6);
        for (int i = 0; i < max; i++) {
            compLabels.get(i).setText(competences.get(i).getIntitule());
        }
    }

    /**
     * Handles the action of clicking the "Accueil" button.
     * 
     * This method loads the PageAccueilSecouriste interface, passing the current user data to the controller,
     * and updates the stage scene to the newly loaded interface. It catches and prints any IOExceptions that occur
     * during this process.
     * 
     * @param event the ActionEvent triggered by clicking the "Accueil" button
     */
    @FXML
    public void accueilPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PageAccueilSecouriste.fxml"));
            Parent root = loader.load();

            PageAccueilSecouristeController controller = loader.getController();
            controller.setUser(user);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur de navigation", "Impossible de charger la page d'accueil");
            e.printStackTrace();
        }
    }

    /**
     * Handles the action of clicking the "Ajouter une disponibilit " button.
     * 
     * This method loads the PageAccueilAjoutDispo interface, passing the current user data to the controller,
     * and updates the stage scene to the newly loaded interface. It catches and prints any IOExceptions that occur
     * during this process.
     * 
     * @param event the ActionEvent triggered by clicking the "Ajouter une disponibilit " button
     */
    @FXML
    public void ajouterDisponibilite(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PageAccueilAjoutDispo.fxml"));
            Parent root = loader.load();

            PageAjoutDispoController controller = loader.getController();
            controller.setUser(user);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur de navigation", "Impossible de charger la page d'accueil");
            e.printStackTrace();
        }
    }

    /**
     * Shows an alert with the given title and message.
     * 
     * This method is a utility method used to show an alert with a given title and message.
     * The alert is an INFORMATION type alert, and it is showed using the showAndWait() method.
     * 
     * @param title the title of the alert
     * @param message the message to display in the alert
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}