package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.data.persistence.Dispos;
import model.data.persistence.Secouriste;
import model.data.users.UserSecouriste;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.EnumMap;
import java.util.Map;

public class PagePlanningController {
    private UserSecouriste user;
    private Secouriste secouriste;
    private LocalDate currentWeekStart;

    @FXML
    private TableView<Map<DayOfWeek, String>> IDTableauPlanning;
    @FXML
    private TableColumn<Map<DayOfWeek, String>, String> nomColumn;
    @FXML
    private TableColumn<Map<DayOfWeek, String>, String> ColonneLundi;
    @FXML
    private TableColumn<Map<DayOfWeek, String>, String> ColonneMardi;
    @FXML
    private TableColumn<Map<DayOfWeek, String>, String> ColonneMercredi;
    @FXML
    private TableColumn<Map<DayOfWeek, String>, String> ColonneJeudi;
    @FXML
    private TableColumn<Map<DayOfWeek, String>, String> ColonneVendredi;
    @FXML
    private TableColumn<Map<DayOfWeek, String>, String> ColonneSamedi;
    @FXML
    private TableColumn<Map<DayOfWeek, String>, String> ColonneDimanche;

    @FXML
    private Button accueilButton;

    @FXML
    private Hyperlink IDPagePrecedente;
    @FXML
    private Hyperlink IDPageSuivante;

    public void setUser(UserSecouriste user) {
        this.user = user;
        this.secouriste = user.getSecouriste();
        this.currentWeekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        loadWeekData();
    }

    @FXML
    private void initialize() {
        // Configuration des colonnes
        nomColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(secouriste != null ? secouriste.getNomComplet() : ""));
        
        ColonneLundi.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().get(DayOfWeek.MONDAY)));
        ColonneMardi.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().get(DayOfWeek.TUESDAY)));
        ColonneMercredi.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().get(DayOfWeek.WEDNESDAY)));
        ColonneJeudi.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().get(DayOfWeek.THURSDAY)));
        ColonneVendredi.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().get(DayOfWeek.FRIDAY)));
        ColonneSamedi.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().get(DayOfWeek.SATURDAY)));
        ColonneDimanche.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().get(DayOfWeek.SUNDAY)));

        // Configuration du style des cellules
        configureCellStyle(ColonneLundi);
        configureCellStyle(ColonneMardi);
        configureCellStyle(ColonneMercredi);
        configureCellStyle(ColonneJeudi);
        configureCellStyle(ColonneVendredi);
        configureCellStyle(ColonneSamedi);
        configureCellStyle(ColonneDimanche);

        // Gestion des événements
        IDPagePrecedente.setOnAction(event -> handleSemainePrecedente());
        IDPageSuivante.setOnAction(event -> handleSemaineSuivante());
    }

    private void configureCellStyle(TableColumn<Map<DayOfWeek, String>, String> column) {
        column.setCellFactory(col -> new TableCell<Map<DayOfWeek, String>, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.toLowerCase().contains("indisponible")) {
                        setStyle("-fx-background-color: lightcoral; -fx-text-fill: white;");
                    } else {
                        setStyle("-fx-background-color: lightgreen;");
                    }
                }
            }
        });
    }

    private void loadWeekData() {
        if (secouriste == null) return;
        
        Map<DayOfWeek, String> weekData = new EnumMap<>(DayOfWeek.class);
        
        // Initialiser tous les jours comme indisponibles
        for (DayOfWeek day : DayOfWeek.values()) {
            weekData.put(day, "Indisponible");
        }
        
        // Remplir les disponibilités existantes
        for (Dispos dispos : secouriste.getDisponibilites()) {
            DayOfWeek day = dispos.getLocalDate().getDayOfWeek();
            String timeRange = String.format("%02d:%02d-%02d:%02d",
                dispos.getHeureDebut()[0], dispos.getHeureDebut()[1],
                dispos.getHeureFin()[0], dispos.getHeureFin()[1]);
            
            weekData.put(day, timeRange);
        }

        ObservableList<Map<DayOfWeek, String>> items = FXCollections.observableArrayList();
        items.add(weekData);
        IDTableauPlanning.setItems(items);
    }

    @FXML
    private void handleSemainePrecedente() {
        currentWeekStart = currentWeekStart.minusWeeks(1);
        loadWeekData();
    }

    @FXML
    private void handleSemaineSuivante() {
        currentWeekStart = currentWeekStart.plusWeeks(1);
        loadWeekData();
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}