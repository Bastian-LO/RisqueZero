package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.data.persistence.Secouriste;
import model.data.service.DAOMngt;
import model.data.users.UserAdmin;
import model.data.users.UserSecouriste;

import java.io.IOException;

/**
 * Controller for the gestion secouristes page
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */
public class PageGestionSecouristesController {

    @FXML private ListView<Secouriste> secouristesListView;
    @FXML public Button ajouterButton;

    public UserAdmin user;

    /**
     * Initializes the controller. Called after the FXML file has been loaded
     * @param user the current user
     */
    @FXML
    public void initialize(UserAdmin user) {
        secouristesListView.getItems().setAll(DAOMngt.getSecouristeDAO().findAll());
        this.user = user;
        secouristesListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double clic
                Secouriste selected = secouristesListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    ouvrirModificationSecouriste(selected);
                }
            }
        });
    }

    /**
     * Handles the event when the "Ajouter" button is clicked.
     * This method should open a new window or dialog for adding a new secouriste.
     */
    @FXML
    public void handleAjouterSecouriste() {

    }

    /**
     * Opens a new window or dialog for modifying a secouriste
     * @param secouriste the secouriste to modify
     */
    private void ouvrirModificationSecouriste(Secouriste secouriste) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PageModifSecouriste.fxml"));
            Parent root = loader.load();
            PageModifSecouristeController controller = loader.getController();
            controller.setSecouriste(secouriste);
            controller.setUser(user);
            Stage  stage = (Stage) secouristesListView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}