package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.data.users.UserAdmin;

public class PageAjoutDPSController {
    
    private UserAdmin user;
    
    @FXML private DatePicker Calendrier;
    @FXML private TextField sportTextField;
    @FXML private TextField siteTextField;
    @FXML private ListView<String> ListeCompetence;
    @FXML private ComboBox<String> ComboBoxCompetence;
    @FXML private TextField heureDebutTextField;
    @FXML private TextField minuteDebutTextField;
    @FXML private TextField heureFinTextField;
    @FXML private TextField minuteFinTextField;
    @FXML private Label dateLabel;
    
    public void initialize(UserAdmin user) {
        this.user = user;
        // Initialisation des données
    }
    
    // Méthodes de gestion des événements
    @FXML public void sportHandle() {}
    @FXML public void siteHandle() {}
    @FXML public void ComboBoxCompétenceHandle() {}
    @FXML public void CalendrierHandle() {}
    @FXML public void heureDebutHandle() {}
    @FXML public void minuteDebutHandle() {}
    @FXML public void heureFinHandle() {}
    @FXML public void minuteFinHandle() {}
    @FXML public void BoutonEnregistrerHandle() {}

    public void BoutonAccueilHandle(ActionEvent actionEvent) {
    }

    public void ListeCompétenceHandle(ListView.EditEvent editEvent) {
    }

    public void backHandle(MouseEvent mouseEvent) {
    }
}