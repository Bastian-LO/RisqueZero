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

public class PageGestionSecouristesController {

    @FXML private ListView<Secouriste> secouristesListView;
    @FXML private Button ajouterButton;

    public UserAdmin user;

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

    @FXML
    public void handleAjouterSecouriste() {

    }

    private void ouvrirModificationSecouriste(Secouriste secouriste) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PageModifSecouriste.fxml"));
            Parent root = loader.load();
            PageModifSecouristeController controller = loader.getController();
            controller.setSecouriste(secouriste);
            controller.setUser(user);
            Stage  stage = (Stage) ajouterButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}