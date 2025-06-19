package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.data.persistence.DPS;
import model.data.persistence.Site;
import model.data.persistence.Sport;
import model.data.service.DAOMngt;
import model.data.users.UserAdmin;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

public class PageDPSController {

    private UserAdmin user;
    private LocalDate currentWeekStart;
    private Map<DayOfWeek, List<DPS>> dpsByDay = new EnumMap<>(DayOfWeek.class);

    @FXML private Button BoutonAccueil;
    @FXML private Button BoutonAjoutDPS;
    @FXML private Hyperlink LienSemainePrécédente;
    @FXML private Hyperlink LienSemaineSuivante;
    @FXML private TableView<DPSRow> tableView;
    @FXML private TableColumn<DPSRow, String> lundiColumn;
    @FXML private TableColumn<DPSRow, String> mardiColumn;
    @FXML private TableColumn<DPSRow, String> mercrediColumn;
    @FXML private TableColumn<DPSRow, String> jeudiColumn;
    @FXML private TableColumn<DPSRow, String> vendrediColumn;
    @FXML private TableColumn<DPSRow, String> samediColumn;
    @FXML private TableColumn<DPSRow, String> dimancheColumn;
    @FXML private Label titreLabel;

    public void initialize(UserAdmin user) {
        this.user = user;
        this.currentWeekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        
        configureTableView();
        loadWeekData();
        setupEventHandlers();
        updateTitle();
    }

    private void configureTableView() {
        // Configuration des colonnes
        lundiColumn.setCellValueFactory(new PropertyValueFactory<>("lundi"));
        mardiColumn.setCellValueFactory(new PropertyValueFactory<>("mardi"));
        mercrediColumn.setCellValueFactory(new PropertyValueFactory<>("mercredi"));
        jeudiColumn.setCellValueFactory(new PropertyValueFactory<>("jeudi"));
        vendrediColumn.setCellValueFactory(new PropertyValueFactory<>("vendredi"));
        samediColumn.setCellValueFactory(new PropertyValueFactory<>("samedi"));
        dimancheColumn.setCellValueFactory(new PropertyValueFactory<>("dimanche"));

        // Style des cellules
        TableColumn<?, ?>[] columns = {lundiColumn, mardiColumn, mercrediColumn, jeudiColumn, 
                                      vendrediColumn, samediColumn, dimancheColumn};
        
        for (TableColumn<?, ?> col : columns) {
            col.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        setStyle("-fx-background-color: #e0f7fa; -fx-border-color: #b2ebf2;");
                    }
                }
            });
        }

        // Gestion du clic sur une cellule
        tableView.setRowFactory(tv -> {
            TableRow<DPSRow> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) {
                    DPSRow clickedRow = row.getItem();
                    TablePosition<DPSRow, ?> pos = row.getTableView().getSelectionModel().getSelectedCells().get(0);
                    int columnIndex = pos.getColumn();
                    
                    DayOfWeek day = getDayFromColumnIndex(columnIndex);
                    List<DPS> dayDps = dpsByDay.get(day);
                    
                    if (dayDps != null && clickedRow != null) {
                        int rowIndex = row.getIndex();
                        if (rowIndex < dayDps.size()) {
                            DPS selectedDPS = dayDps.get(rowIndex);
                            openModifDPSPage(selectedDPS);
                        }
                    }
                }
            });
            return row;
        });
    }

    private DayOfWeek getDayFromColumnIndex(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> DayOfWeek.MONDAY;
            case 1 -> DayOfWeek.TUESDAY;
            case 2 -> DayOfWeek.WEDNESDAY;
            case 3 -> DayOfWeek.THURSDAY;
            case 4 -> DayOfWeek.FRIDAY;
            case 5 -> DayOfWeek.SATURDAY;
            case 6 -> DayOfWeek.SUNDAY;
            default -> null;
        };
    }

    private void openModifDPSPage(DPS dps) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PageModifDPS.fxml"));
            Parent root = loader.load();
            
            PageModifDPSController controller = loader.getController();
            controller.initialize(user, dps);
            
            Stage stage = (Stage) tableView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la page de modification: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void loadWeekData() {
        // Récupérer les DPS pour la semaine courante
        LocalDate weekEnd = currentWeekStart.plusDays(6);
        List<DPS> dpsList = DAOMngt.getDPSDAO().findByDateRange(currentWeekStart, weekEnd);
        
        // Réinitialiser la map
        dpsByDay.clear();
        
        // Grouper par jour de la semaine et trier par heure de début
        for (DayOfWeek day : DayOfWeek.values()) {
            List<DPS> dayDps = dpsList.stream()
                .filter(dps -> dps.getDateEvt().toLocalDate().getDayOfWeek() == day)
                .sorted(Comparator.comparing(dps -> {
                    int[] heureDebut = dps.getHoraireDepart();
                    return heureDebut[0] * 60 + heureDebut[1];
                }))
                .collect(Collectors.toList());
            
            dpsByDay.put(day, dayDps);
        }
        
        // Créer les lignes pour le tableau
        ObservableList<DPSRow> rows = FXCollections.observableArrayList();
        
        // Trouver le nombre maximum de DPS par jour
        int maxDpsPerDay = dpsByDay.values().stream()
            .mapToInt(List::size)
            .max()
            .orElse(0);
        
        // Créer une ligne par créneau DPS
        for (int i = 0; i < maxDpsPerDay; i++) {
            DPSRow row = new DPSRow();
            
            for (DayOfWeek day : DayOfWeek.values()) {
                List<DPS> dayDps = dpsByDay.getOrDefault(day, List.of());
                if (i < dayDps.size()) {
                    DPS dps = dayDps.get(i);
                    String dpsText = formatDps(dps);
                    row.setDayValue(day, dpsText);
                }
            }
            rows.add(row);
        }
        
        tableView.setItems(rows);
        updateTitle();
    }

    private String formatDps(DPS dps) {
        Site site = DAOMngt.getSiteDAO().findByCode(dps.getLieu().getCode());
        Sport sport = DAOMngt.getSportDAO().findByCode(dps.getSport().getCode());
        
        return String.format("%s - %s\n%s:%s - %s:%s", 
            sport != null ? sport.getNom() : "Inconnu",
            site != null ? site.getNom() : "Inconnu",
            String.format("%02d", dps.getHoraireDepart()[0]),
            String.format("%02d", dps.getHoraireDepart()[1]),
            String.format("%02d", dps.getHoraireFin()[0]),
            String.format("%02d", dps.getHoraireFin()[1]));
    }

    private void setupEventHandlers() {
        LienSemainePrécédente.setOnAction(event -> {
            currentWeekStart = currentWeekStart.minusWeeks(1);
            loadWeekData();
        });
        
        LienSemaineSuivante.setOnAction(event -> {
            currentWeekStart = currentWeekStart.plusWeeks(1);
            loadWeekData();
        });
        
        BoutonAccueil.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PageAccueilAdmin.fxml"));
                Parent root = loader.load();
                
                Stage stage = (Stage) BoutonAccueil.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible de retourner à l'accueil: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
        
        BoutonAjoutDPS.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/PageAjoutDPS.fxml"));
                Parent root = loader.load();
                
                PageAjoutDPSController controller = loader.getController();
                controller.initialize(user);
                
                Stage stage = (Stage) BoutonAjoutDPS.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir la page d'ajout: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    private void updateTitle() {
        int weekNumber = currentWeekStart.get(WeekFields.ISO.weekOfWeekBasedYear());
        titreLabel.setText("Dispositifs : Semaine " + weekNumber);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Classe interne représentant une ligne dans le tableau DPS
     */
    public static class DPSRow {
        private String lundi;
        private String mardi;
        private String mercredi;
        private String jeudi;
        private String vendredi;
        private String samedi;
        private String dimanche;

        public DPSRow() {
            this.lundi = "";
            this.mardi = "";
            this.mercredi = "";
            this.jeudi = "";
            this.vendredi = "";
            this.samedi = "";
            this.dimanche = "";
        }

        public void setDayValue(DayOfWeek day, String value) {
            switch (day) {
                case MONDAY: lundi = value; break;
                case TUESDAY: mardi = value; break;
                case WEDNESDAY: mercredi = value; break;
                case THURSDAY: jeudi = value; break;
                case FRIDAY: vendredi = value; break;
                case SATURDAY: samedi = value; break;
                case SUNDAY: dimanche = value; break;
            }
        }

        // Getters
        public String getLundi() { return lundi; }
        public String getMardi() { return mardi; }
        public String getMercredi() { return mercredi; }
        public String getJeudi() { return jeudi; }
        public String getVendredi() { return vendredi; }
        public String getSamedi() { return samedi; }
        public String getDimanche() { return dimanche; }
    }
}