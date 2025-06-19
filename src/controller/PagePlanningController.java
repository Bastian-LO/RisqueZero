package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.data.persistence.Dispos;
import model.data.persistence.Secouriste;
import model.data.users.UserSecouriste;
import model.dao.DisposDAO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class PagePlanningController {
    private UserSecouriste user;
    private Secouriste secouriste;
    private LocalDate currentWeekStart;

    @FXML
    private TableView<PlanningRow> tableView;

    @FXML
    private TableColumn<PlanningRow, String> nomColumn;
    @FXML
    private TableColumn<PlanningRow, String> lundiColumn;
    @FXML
    private TableColumn<PlanningRow, String> mardiColumn;
    @FXML
    private TableColumn<PlanningRow, String> mercrediColumn;
    @FXML
    private TableColumn<PlanningRow, String> jeudiColumn;
    @FXML
    private TableColumn<PlanningRow, String> vendrediColumn;
    @FXML
    private TableColumn<PlanningRow, String> samediColumn;
    @FXML
    private TableColumn<PlanningRow, String> dimancheColumn;

    public void setUser(UserSecouriste user) {
        this.user = user;
        this.secouriste = user.getSecouriste();
        this.currentWeekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        loadWeekData();
    }

    @FXML
    private void initialize() {
        // Configuration des colonnes
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        lundiColumn.setCellValueFactory(new PropertyValueFactory<>("lundi"));
        mardiColumn.setCellValueFactory(new PropertyValueFactory<>("mardi"));
        mercrediColumn.setCellValueFactory(new PropertyValueFactory<>("mercredi"));
        jeudiColumn.setCellValueFactory(new PropertyValueFactory<>("jeudi"));
        vendrediColumn.setCellValueFactory(new PropertyValueFactory<>("vendredi"));
        samediColumn.setCellValueFactory(new PropertyValueFactory<>("samedi"));
        dimancheColumn.setCellValueFactory(new PropertyValueFactory<>("dimanche"));

        // Style des cellules
        TableColumn[] jours = {lundiColumn, mardiColumn, mercrediColumn, jeudiColumn, 
                             vendrediColumn, samediColumn, dimancheColumn};
        
        for (TableColumn<PlanningRow, String> col : jours) {
            col.setCellFactory(column -> new TableCell<>() {
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
    }

    /**
     * Loads the planning data for the current week of the secouriste.
     */
    private void loadWeekData() {
        DisposDAO disposDAO = new DisposDAO();
        HashSet<Dispos> allDispos = disposDAO.findBySecouriste(secouriste.getId());
        
        // Créer une ligne pour le secouriste
        PlanningRow row = new PlanningRow(secouriste.getNom() + " " + secouriste.getPrenom());
        
        // Remplir les disponibilités pour chaque jour de la semaine
        for (int i = 0; i < 7; i++) {
            LocalDate day = currentWeekStart.plusDays(i);
            List<Dispos> dayDispos = allDispos.stream()
                .filter(d -> d.getDate().toLocalDate().equals(day))
                .collect(Collectors.toList());
            
            String disponibilites = dayDispos.isEmpty() 
                ? "Indisponible" 
                : dayDispos.stream()
                    .map(d -> String.format("%02d:%02d-%02d:%02d", 
                          d.getHeureDebut()[0], d.getHeureDebut()[1],
                          d.getHeureFin()[0], d.getHeureFin()[1]))
                    .collect(Collectors.joining(", "));
            
            switch (day.getDayOfWeek()) {
                case MONDAY: row.setLundi(disponibilites); break;
                case TUESDAY: row.setMardi(disponibilites); break;
                case WEDNESDAY: row.setMercredi(disponibilites); break;
                case THURSDAY: row.setJeudi(disponibilites); break;
                case FRIDAY: row.setVendredi(disponibilites); break;
                case SATURDAY: row.setSamedi(disponibilites); break;
                case SUNDAY: row.setDimanche(disponibilites); break;
            }
        }
        
        tableView.setItems(FXCollections.observableArrayList(row));
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

    /**
     * Internal class representing a row in the planning table.
     */
    public static class PlanningRow {
        private final String nom;
        private String lundi;
        private String mardi;
        private String mercredi;
        private String jeudi;
        private String vendredi;
        private String samedi;
        private String dimanche;

        public PlanningRow(String nom) {
            this.nom = nom;
            this.lundi = "";
            this.mardi = "";
            this.mercredi = "";
            this.jeudi = "";
            this.vendredi = "";
            this.samedi = "";
            this.dimanche = "";
        }

        // Getters et setters
        public String getNom() { return nom; }
        public String getLundi() { return lundi; }
        public void setLundi(String lundi) { this.lundi = lundi; }
        public String getMardi() { return mardi; }
        public void setMardi(String mardi) { this.mardi = mardi; }
        public String getMercredi() { return mercredi; }
        public void setMercredi(String mercredi) { this.mercredi = mercredi; }
        public String getJeudi() { return jeudi; }
        public void setJeudi(String jeudi) { this.jeudi = jeudi; }
        public String getVendredi() { return vendredi; }
        public void setVendredi(String vendredi) { this.vendredi = vendredi; }
        public String getSamedi() { return samedi; }
        public void setSamedi(String samedi) { this.samedi = samedi; }
        public String getDimanche() { return dimanche; }
        public void setDimanche(String dimanche) { this.dimanche = dimanche; }
    }
}