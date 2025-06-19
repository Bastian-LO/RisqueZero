package controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.data.persistence.Affectation;
import model.data.persistence.Competence;
import model.data.persistence.DPS;
import model.data.persistence.Dispos;
import model.data.users.UserSecouriste;
import model.data.persistence.Secouriste;
import model.data.service.DAOMngt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PageAccueilSecouristeController {

    @FXML
    public Button planningButton;

    @FXML
    public Button deconnexionButton;

    @FXML
    private Button profilButton;

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
    private Label idEvenementLabel;

    @FXML
    private Label nomEvenementLabel;

    @FXML
    private Label siteEvenementLabel;

    @FXML
    private Label dateEvenementLabel;

    @FXML
    private HBox ContenaireCompetences;

    @FXML
    private HBox ContenaireDispo;

    @FXML
    private VBox ListePlanning;

    private UserSecouriste user;
    private Secouriste secouriste;

    public void setUser(UserSecouriste user) {
        this.user = user;
        this.secouriste = user.getSecouriste();

        Platform.runLater(this::updateUI);
    }

    private void updateUI() {
        if (secouriste != null) {
            updateCompetences(secouriste.getCompetences());

            updateDisponibilites(secouriste.getDisponibilites());

            List<Affectation> affectations = DAOMngt.getAffectationDAO().findBySecouriste(secouriste.getId());
            List<String> planning = new ArrayList<>();
            for (Affectation affectation : affectations) {
                DPS dps = affectation.getDps();
                planning.add(dps.toString());
            }
            updatePlanning(planning);
        }
    }

    private void updateCompetences(ArrayList<Competence> competences) {

        Label[] initCompetenceLabels = {Competence1, Competence2, Competence3,Competence4, Competence5, Competence6};

        int count = 1;
        for (Label label : initCompetenceLabels) {
            if (label == null) {
                System.err.println("Le label de disponibilité " + count + " est null!");
                return;
            }
            label.setText(" ");
            count++;
        }
        if (competences == null || competences.isEmpty()) {
            initCompetenceLabels[0].setText("Pas de compétences enregistrées pour le moment.");
            return;
        }

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
            count = 1;
            for (Label label : initCompetenceLabels) {
                if (label == null) {
                    System.err.println("Le label de disponibilité " + count + " est null!");
                    return;
                }
                label.setText(" ");
                count++;
            }
        }

    }

    private void updateDisponibilites(HashSet<Dispos> disponibilites) {
        Label[] disponibiliteLabels = {Dispo1, Dispo2, Dispo3,
                Dispo4};
        int count = 1;
        for (Label label : disponibiliteLabels) {
            if (label == null) {
                System.err.println("Le label de disponibilité " + count + " est null!");
                return;
            }
            label.setText(" ");
            count++;
        }

        if (disponibilites == null || disponibilites.isEmpty()) {
            disponibiliteLabels[0].setText("Pas de disponibilités enregistrées pour le moment.");
            return;
        }

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

    private void updatePlanning(List<String> planningItems) {
        Label[] planningLabels = {idEvenementLabel, nomEvenementLabel, siteEvenementLabel, dateEvenementLabel};
        int count = 1;
        for (Label label : planningLabels) {
            if (label == null) {
                System.err.println("Le label de planning " + count + " est null!");
                return;
            }
            label.setText(" ");
            count++;
        }

        if (planningItems != null && !planningItems.isEmpty()) {
            for (int i = 0; i < Math.min(planningItems.size(), 4); i++) {
                switch (i) {
                    case 0:
                        idEvenementLabel.setText("- " + planningItems.get(i));
                        break;
                    case 1:
                        nomEvenementLabel.setText("- " + planningItems.get(i));
                        break;
                    case 2:
                        siteEvenementLabel.setText("- " + planningItems.get(i));
                        break;
                    case 3:
                        dateEvenementLabel.setText("- " + planningItems.get(i));
                        break;
                }
            }
        }
    }

    @FXML
    public void handleVoirProfil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PageProfilSecouristes.fxml"));
            Parent root = loader.load();

            PageProfilSecouristeController controller = loader.getController();
            controller.setUser(user);
            Stage stage = (Stage) profilButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleVoirPlanning(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PagePlanning.fxml"));
            Parent root = loader.load();

            PagePlanningController controller = loader.getController();
            controller.setUser(user);


            Stage stage = (Stage) profilButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deconnexionPage(ActionEvent actionEvent) {
        try {
            Parent connexionRoot = FXMLLoader.load(getClass().getResource("../resources/fxml/PageConnexion.fxml"));

            Scene connexionScene = new Scene(connexionRoot);
            
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.setScene(connexionScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}