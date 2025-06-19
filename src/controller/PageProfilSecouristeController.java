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

    public void setUser(UserSecouriste user) {
        this.user = user;
        this.secouriste = user.getSecouriste();
        updateUI();
    }



    private void updateUI() {
        // Mise à jour des informations personnelles
        Info1.setText("- ID : " + this.secouriste.getId());
        Info2.setText("- Nom et Prénom : " + secouriste.getNom() + " " + secouriste.getPrenom());
        Info3.setText("- Date de naissance : " + secouriste.getDateNaissance());
        Info4.setText("- Adresse mail : " + secouriste.getEmail());
        Info5.setText("- Téléphone : " + secouriste.getTel());
        Info6.setText("- Adresse : " + secouriste.getAdresse());

        // Mise à jour des disponibilités
        updateDisponibilites();

        // Mise à jour des compétences
        updateCompetences();
    }

    private void updateDisponibilites() {
        List<Dispos> dispos = new ArrayList<>(secouriste.getDisponibilites());

        // Réinitialiser toutes les labels
        List<Label> dispoLabels = List.of(Dispo1, Dispo2, Dispo3, Dispo4, Dispo5, Dispo6);
        dispoLabels.forEach(label -> label.setText(""));

        // Afficher jusqu'à 6 disponibilités
        int max = Math.min(dispos.size(), 6);
        for (int i = 0; i < max; i++) {
            Dispos d = dispos.get(i);
            String text = d.toString();
            dispoLabels.get(i).setText(text);
        }
    }

    private void updateCompetences() {
        List<Competence> competences = new ArrayList<>(secouriste.getCompetences());

        // Réinitialiser toutes les labels
        List<Label> compLabels = List.of(Comp1, Comp2, Comp3, Comp4, Comp5, Comp6);
        compLabels.forEach(label -> label.setText(""));

        // Afficher jusqu'à 6 compétences
        int max = Math.min(competences.size(), 6);
        for (int i = 0; i < max; i++) {
            compLabels.get(i).setText(competences.get(i).getIntitule());
        }
    }

    @FXML
    public void accueilPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PageAccueilSecouriste.fxml"));
            Parent root = loader.load();

            // Passage des données utilisateur
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

    @FXML
    public void ajouterDisponibilite(ActionEvent event) {
        // TODO Fonctionnalité à implémenter
        showAlert("Fonctionnalité en développement", "L'ajout de disponibilité sera disponible prochainement");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}