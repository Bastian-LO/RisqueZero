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

/**
 * Controller for the planning page
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */

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

/**
 * Sets the current user and initializes the related secouriste and current week start.
 * Loads the data for the current week.
 * 
 * @param user the UserSecouriste object to be set
 */
    public void setUser(UserSecouriste user) {
        this.user = user;
        this.secouriste = user.getSecouriste();
        this.currentWeekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        loadWeekData();
    }


    /**
     * Initializes the controller after the FXML file has been loaded.
     * Sets the cell value factories for the columns and configures the cell style.
     * Sets the on action for the previous and next week hyperlinks.
     */
    @FXML
    private void initialize() {
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

        configureCellStyle(ColonneLundi);
        configureCellStyle(ColonneMardi);
        configureCellStyle(ColonneMercredi);
        configureCellStyle(ColonneJeudi);
        configureCellStyle(ColonneVendredi);
        configureCellStyle(ColonneSamedi);
        configureCellStyle(ColonneDimanche);

        IDPagePrecedente.setOnAction(event -> handleSemainePrecedente());
        IDPageSuivante.setOnAction(event -> handleSemaineSuivante());
    }

    /**
     * Configures the style of the cells in the given column.
     * If the cell is empty, it sets the text to null and the style to an empty string.
     * If the cell contains the string "indisponible", it sets the background color to lightcoral and the text fill to white.
     * Otherwise, it sets the background color to lightgreen.
     * @param column the TableColumn to be configured
     */
    private void configureCellStyle(TableColumn<Map<DayOfWeek, String>, String> column) {
        column.setCellFactory(col -> new TableCell<Map<DayOfWeek, String>, String>() {
/**
 * Updates the visual representation of a cell in the table.
 * 
 * If the cell is empty or the item is null, it clears the text and style.
 * For non-empty cells, it sets the displayed text to the item value.
 * Applies a specific style if the item contains the string "indisponible",
 * setting the background to lightcoral and text color to white.
 * Otherwise, applies a lightgreen background.
 * 
 * @param item the text to display in the cell
 * @param empty indicates whether this cell is empty
 */
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

    /**
     * Loads the week data for the secouriste in the table.
     * 
     * If the secouriste is null, does nothing.
     * 
     * Initializes all days as unavailable.
     * Then, for each availability of the secouriste, it sets the corresponding day to
     * a string in the format "HH:MM-HH:MM" where HH:MM is the start and end time of the availability.
     * Finally, it sets the items of the table to an observable list containing the week data.
     */
    private void loadWeekData() {
        if (secouriste == null) return;
        
        Map<DayOfWeek, String> weekData = new EnumMap<>(DayOfWeek.class);
        
        for (DayOfWeek day : DayOfWeek.values()) {
            weekData.put(day, "Indisponible");
        }
        
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

    /**
     * Handles the button to go to the previous week.
     * Goes back by one week and reloads the week data.
     */
    @FXML
    private void handleSemainePrecedente() {
        currentWeekStart = currentWeekStart.minusWeeks(1);
        loadWeekData();
    }

    /**
     * Handles the button to go to the next week.
     * Goes forward by one week and reloads the week data.
     */
    @FXML
    private void handleSemaineSuivante() {
        currentWeekStart = currentWeekStart.plusWeeks(1);
        loadWeekData();
    }

    /**
     * Handles the button to go back to the home page.
     * Goes back to the home page and passes the user to the controller.
     * @param event the event that triggered this method
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
     * Shows an alert with the given title and message.
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