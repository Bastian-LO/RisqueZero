package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.data.persistence.DPS;
import model.data.persistence.Secouriste;
import model.data.service.DAOMngt;
import model.data.users.UserAdmin;

import java.io.IOException;
import java.util.List;

public class PageAffectationController {

    @FXML private Button BoutonAccueil;
    @FXML private Button BoutonEnregistrer;
    @FXML private TextField BarreDeRecherche;
    @FXML private ListView<Secouriste> listeSecouristes;

    private UserAdmin user;
    private DPS dps;
    private ObservableList<Secouriste> secouristesData = FXCollections.observableArrayList();
    private FilteredList<Secouriste> filteredData = new FilteredList<>(secouristesData, p -> true);

    public void initialize(UserAdmin user, DPS dps) {
        this.user = user;
        this.dps = dps;
        initSecouristesList();
        setupSearch();
    }

    private void initSecouristesList() {
        // Récupération des secouristes depuis la base de données
        List<Secouriste> secouristes = DAOMngt.getSecouristeDAO().findAll();
        secouristesData.setAll(secouristes);

        // Configuration de la ListView
        listeSecouristes.setItems(filteredData);
        listeSecouristes.setCellFactory(lv -> new ListCell<Secouriste>() {
            @Override
            protected void updateItem(Secouriste secouriste, boolean empty) {
                super.updateItem(secouriste, empty);
                if (empty || secouriste == null) {
                    setText(null);
                } else {
                    setText(secouriste.getNom() + " " + secouriste.getPrenom() +
                            " (" + secouriste.getCompetences().size() + " compétences)");
                }
            }
        });

        // Sélection multiple
        listeSecouristes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void setupSearch() {
        BarreDeRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(secouriste -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return secouriste.getNom().toLowerCase().contains(lowerCaseFilter) ||
                        secouriste.getPrenom().toLowerCase().contains(lowerCaseFilter) ||
                        secouriste.getEmail().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    @FXML
    public void BoutonAccueilHandle(ActionEvent event) {
        naviguerVersPage("../resources/fxml/PageAccueilAdmin.fxml", event);
    }

    @FXML
    public void BoutonEnregistrerHandle(ActionEvent event) {
        // TODO
    }

    @FXML
    public void BarreDeRechercheHandle(ActionEvent event) {
        // La recherche est déjà gérée par le listener
        // On peut forcer le focus sur la liste après la recherche
        if (!listeSecouristes.getItems().isEmpty()) {
            listeSecouristes.requestFocus();
            listeSecouristes.getSelectionModel().selectFirst();
        }
    }

    private void naviguerVersPage(String fxmlPath, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Passage des données à la page d'accueil admin
            PageAccueilAdminController controller = loader.getController();
            controller.setUser(user);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            afficherErreur("Erreur de navigation", "Impossible de charger la page demandée");
            e.printStackTrace();
        }
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}