package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.data.persistence.Competence;
import model.data.persistence.DPS;
import model.data.service.DAOMngt;
import model.data.users.UserAdmin;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PageModifDPSController {

    // Déclaration des éléments FXML
    @FXML
    private Button BouttonAccueil;
    @FXML
    private Button BouttonAffecter;
    @FXML
    private Button BouttonEnregistrer;
    @FXML
    private Button BouttonSupprimer;
    @FXML
    private ComboBox<String> ComboBoxSport;
    @FXML
    private ComboBox<String> ComboBoxSite;
    @FXML
    private ComboBox<String> ComboBoxCompetences;
    @FXML
    private ListView<String> ListeDeCompetences;
    @FXML
    private DatePicker Calendrier;
    @FXML
    private Label LabelQuiAfficheHeureMinute;

    // Champs pour stocker les données
    private DPS dpsEnCours;

    private UserAdmin user;

    // Méthode pour initialiser les données du DPS
    public void setDPS(DPS dps) {
        this.dpsEnCours = dps;
        chargerDonneesDPS();
    }

    // Initialisation du contrôleur
    @FXML
    public void initialize(UserAdmin user) {
        // Peuplement des ComboBox avec des données
        ComboBoxSport.getItems().addAll("Skateboard", "Breakdance", "Ski de fond", "Kamping");
        ComboBoxSite.getItems().addAll("Paris", "Vannes", "Beijing", "Antananarivo");
        ComboBoxCompetences.getItems().addAll("PSC1", "PSE1", "PSE2", "DEA", "BNSSA");

        // Configuration du DatePicker
        Calendrier.setValue(LocalDate.now());

        this.user = user;

    }

    private void chargerDonneesDPS() {
        if (dpsEnCours != null) {
            ComboBoxSport.setValue(dpsEnCours.getSport().getNom());
            ComboBoxSite.setValue(dpsEnCours.getLieu().getNom());
            Calendrier.setValue(dpsEnCours.getDateEvt().toLocalDate());

            List<Competence> competences = DAOMngt.getCompetenceDAO().findAll();
            List<Competence> competencesManquantes = new ArrayList<>();
            for (Competence competence : competences) {
                if (!dpsEnCours.getCompetences().contains(competence)) {
                    competencesManquantes.add(competence);
                }
                List<Competence> sublist = competencesManquantes.subList(0, Math.min(5, competences.size()));
                for (Competence comp : sublist) {
                    ListeDeCompetences.getItems().add(comp.getIntitule());
                }
            }
        }
    }

    // Handlers définis dans le FXML
    @FXML
    public void BouttonAccueilHandle(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PageDPS.fxml"));
        try {
            Parent root = loader.load();
            PageDPSController controller = loader.getController();
            controller.initialize(user);
            Stage stage = (Stage) BouttonAccueil.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (
                IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void BouttonAffecterHandle(ActionEvent event) {
        System.out.println("Affectation des secouristes");
        // Logique d'affectation à implémenter
    }

    @FXML
    public void BouttonEnregistrerHandle(ActionEvent event) {
        System.out.println("Enregistrement des modifications");
        // Validation et sauvegarde des modifications
        sauvegarderModifications();
    }

    @FXML
    public void BouttonSupprimerHandle(ActionEvent event) {
        System.out.println("Suppression du DPS");
        // Confirmation et suppression
        confirmerSuppression();
    }

    @FXML
    public void sportHandle(ActionEvent event) {
        System.out.println("Sport sélectionné: " + ComboBoxSport.getValue());
    }

    @FXML
    public void siteHandle(ActionEvent event) {
        System.out.println("Site sélectionné: " + ComboBoxSite.getValue());
    }

    @FXML
    public void ComboBoxCompetencesHandle(ActionEvent event) {
        String competenceSelectionnee = ComboBoxCompetences.getValue();
        this.dpsEnCours.getCompetences().add(DAOMngt.getCompetenceDAO().findByIntitule(competenceSelectionnee));

        if (competenceSelectionnee != null && !ListeDeCompetences.getItems().contains(competenceSelectionnee)) {
            ListeDeCompetences.getItems().add(competenceSelectionnee);
        }
    }

    // Méthodes utilitaires
    private void sauvegarderModifications() {
        // Logique de sauvegarde à implémenter
        System.out.println("DPS mis à jour avec :");
        System.out.println("- Sport: " + ComboBoxSport.getValue());
        System.out.println("- Site: " + ComboBoxSite.getValue());
        System.out.println("- Date: " + Calendrier.getValue());
        System.out.println("- Compétences: " + Arrays.toString(ListeDeCompetences.getItems().toArray()));

        // Afficher une confirmation
        afficherAlerte("Succès", "Modifications enregistrées avec succès");
    }

    private void confirmerSuppression() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le dispositif ?");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer définitivement ce DPS ?");

        if (alert.showAndWait().isPresent() && alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("DPS supprimé: " + dpsEnCours.getId());
            DAOMngt.getDPSDAO().delete(dpsEnCours);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PageAccueilAdmin.fxml"));
            try {
                Parent root = loader.load();
                PageDPSController controller = loader.getController();
                controller.initialize(user);
                Stage stage = (Stage) BouttonAccueil.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (
                    IOException e) {
                e.printStackTrace();
            }
        }
    }



    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void calendrierHandle(ActionEvent actionEvent) {
        if (Calendrier.getValue() != null) {
            LabelQuiAfficheHeureMinute.setText(Calendrier.getValue().toString());
        }
    }
}