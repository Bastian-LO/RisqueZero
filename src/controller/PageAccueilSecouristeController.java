package controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.data.persistence.Competence;
import model.data.persistence.Dispos;
import model.data.users.UserSecouriste;
import model.data.persistence.Secouriste;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PageAccueilSecouristeController {

    @FXML
    public Button planningBoutton;

    @FXML
    public Button deconnexionButton;

    @FXML
    private Button VoirProfilBoutton;

    @FXML
    private Label Competence1;

    @FXML
    private Label Competence2;

    @FXML
    private Label Competence3;

    @FXML
    private Label Competence4;

    @FXML
    private Label Competence5;

    @FXML
    private Label Competence6;

    @FXML
    private Label Dispo1;

    @FXML
    private Label Dispo2;

    @FXML
    private Label Dispo3;

    @FXML
    private Label Dispo4;

    @FXML
    private Label Dispo5;

    @FXML
    private Label Dispo6;

    @FXML
    private Label LabelID;

    @FXML
    private Label Label2;

    @FXML
    private Label Label3;

    @FXML
    private Label Label4;

    @FXML
    private Label Label5;

    @FXML
    private HBox ContenaireCompetences;

    @FXML
    private HBox ContenaireDispo;

    @FXML
    private VBox ListePlanning;

    private UserSecouriste user;
    private Secouriste secouriste;

    // Méthode pour initialiser les données de l'utilisateur
    public void setUser(UserSecouriste user) {
        this.user = user;
        this.secouriste = user.getSecouriste();
        // Ici vous devriez charger les données du secouriste depuis votre base de données
        // Par exemple: this.secouriste = Database.getSecouriste(user.getIdSecouriste());

        // Mise à jour de l'interface
        updateUI();
    }

    // Met à jour l'interface avec les données du secouriste
    private void updateUI() {
        if (secouriste != null) {
            // Afficher l'ID
            LabelID.setText("- ID : " + secouriste.getId());

            // Mettre à jour les compétences
            updateCompetences(secouriste.getCompetences());

            // Mettre à jour les disponibilités
            updateDisponibilites(secouriste.getDisponibilites());

            // Mettre à jour le planning (exemple simplifié)
            // TODO: Mettre à jour le planning en utilisant la base de données
            updatePlanning(List.of(
                    "Mission 1 - 15/06 - 10h-12h",
                    "Mission 2 - 16/06 - 14h-16h",
                    "Formation - 18/06 - 09h-17h"
            ));
        }
    }

    // Met à jour les labels des compétences
    private void updateCompetences(ArrayList<Competence> competences) {
        // Réinitialiser toutes les compétences
        Competence1.setText("-");
        Competence2.setText("-");
        Competence3.setText("-");
        Competence4.setText("-");
        Competence5.setText("-");
        Competence6.setText("-");

        // Mettre à jour avec les compétences réelles
        if (competences != null) {
            if ( competences.size() < 7 && !competences.isEmpty()) {
                Label[] competenceLabels = {Competence1, Competence2, Competence3,
                        Competence4, Competence5, Competence6};

                for (int i = 0; i < competences.size(); i++) {
                    competenceLabels[i].setText("- " + competences.get(i));
                }
            } else if (competences.size() >= 7) {

                    Label[] competenceLabels = {Competence1, Competence2, Competence3,
                            Competence4, Competence5};

                    for (int i = 0; i < competenceLabels.length; i++) {
                        competenceLabels[i].setText("- " + competences.get(i));
                    }

                    Competence6.setText("..." + competences.get(competences.size() - 1));


            }
        } else {
            // Réinitialiser toutes les compétences
            Competence1.setText("-");
            Competence2.setText("-");
            Competence3.setText("-");
            Competence4.setText("-");
            Competence5.setText("-");
            Competence6.setText("-");
        }

    }

    private void updateDisponibilites(HashSet<Dispos> disponibilites) {
        Label[] disponibiliteLabels = {Dispo1, Dispo2, Dispo3,
                Dispo4, Dispo5, Dispo6};

        // Réinitialiser toutes les disponibilités
        for (Label label : disponibiliteLabels) {
            label.setText("-");
        }

        // Mettre à jour avec les disponibilités réelles
        if (disponibilites == null || disponibilites.isEmpty()) {
            return;
        }

        // Convertir le HashSet en ArrayList pour accéder par index
        ArrayList<Dispos> dispoList = new ArrayList<>(disponibilites);
        int maxDisplay = disponibiliteLabels.length - 1; // On garde une place pour "..."
        int displayCount = Math.min(dispoList.size(), maxDisplay);

        for (int i = 0; i < displayCount; i++) {
            disponibiliteLabels[i].setText("- " + dispoList.get(i));
        }

        if (dispoList.size() > maxDisplay) {
            disponibiliteLabels[maxDisplay].setText("... +" + (dispoList.size() - maxDisplay));
        }
    }
    // Met à jour les éléments du planning
    // TODO: TS
    private void updatePlanning(List<String> planningItems) {
        // Réinitialiser les labels
        Label2.setText("-");
        Label3.setText("-");
        Label4.setText("-");
        Label5.setText("-");

        // Mettre à jour avec les éléments du planning
        if (planningItems != null && !planningItems.isEmpty()) {
            for (int i = 0; i < Math.min(planningItems.size(), 4); i++) {
                switch (i) {
                    case 0:
                        Label2.setText("- " + planningItems.get(i));
                        break;
                    case 1:
                        Label3.setText("- " + planningItems.get(i));
                        break;
                    case 2:
                        Label4.setText("- " + planningItems.get(i));
                        break;
                    case 3:
                        Label5.setText("- " + planningItems.get(i));
                        break;
                }
            }
        }
    }

    // Gère le clic sur le bouton "Voir le profil"
    @FXML
    public void handleVoirProfil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PageProfilSecouristes.fxml"));
            Parent root = loader.load();

            // Passer les données utilisateur au contrôleur de la page de profil
            PageProfilSecouristeController controller = loader.getController();
            controller.setUser(user);
            Stage stage = (Stage) VoirProfilBoutton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Gère le clic sur le bouton "Voir le planning"
    @FXML
    public void handleVoirPlanning(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PagePlanning.fxml"));
            Parent root = loader.load();

            // Passer les données utilisateur au contrôleur de la page de planning
            PagePlanningController controller = loader.getController();
            controller.setUser(user);

            Stage stage = (Stage) VoirProfilBoutton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deconnexionPage(ActionEvent actionEvent) {
        try {
            // Chargement de la deuxième interface depuis second.fxml
            Parent connexionRoot = FXMLLoader.load(getClass().getResource("../resources/fxml/PageConnexion.fxml"));

            // Création de la nouvelle scène
            Scene connexionScene = new Scene(connexionRoot);            // Récupération de la fenêtre actuelle
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Stage stage = (Stage) VoirProfilBoutton.getScene().getWindow();
            stage.setScene(new Scene(connexionRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}