package model.data.graphe;

import model.data.persistence.*;
import model.dao.CompetenceDAO;

import java.time.*;
import java.util.*;

import javafx.util.Pair;

/**
 * The graphe Class is used to create and represent a matrix containing the Secouristes and DPS
 * For affectation. It contains methods to add Secouristes and DPS, and to create the matrix,
 * as well as implementation of the necessary affectation algorithms.
 *
 * @author Emile Thevenin, Killian Avril, Bastian Le Ouedec, Elwan Yvin, Enrick Mananjean
 */
public class Graphe {

    //=================================
    //           ATTRIBUTES
    // ================================

    /**
     * ArrayList representing the DPS associated with each of their competences
     */
    private ArrayList<Pair<DPS, Competence>> DPSCompet = new ArrayList<>();

    /**
     * ArrayList of the Secouristes
     */
    private ArrayList<Secouriste> secouristes = new ArrayList<Secouriste>();

    /**
     * Adjacency matrix used by the algorithms
     */
    int[][] matriceAdj;

    /**
     * Number of peaks in the graph
     */
    int nbSommets;


    //=================================
    //           CONSTRUCTORS
    // ================================

    /**
     * Constructor for the Graphe Class. It requires non-null ArrayLists of Secouristes and DPS.
     * It handles the separation and the association of DPS and their competences.
     * It then initializes the adjacency matrix.
     *
     * @param secouristes ArrayList of Secouristes to add
     * @param dispositifs ArrayList of DPS to add
     * @throws IllegalArgumentException if secouristes or dispositifs is null
     */
    public Graphe(ArrayList<Secouriste> secouristes, ArrayList<DPS> dispositifs) throws IllegalArgumentException {
        if (secouristes == null || dispositifs == null) {
            throw new IllegalArgumentException("Secouristes ou DPS null");
        }
        this.secouristes = secouristes;
        for (DPS dispositif : dispositifs) {
            for (Competence c : dispositif.getCompetences()) {
                DPSCompet.add(new Pair<>(dispositif, c));
            }
        }
        this.initMatriceAdjacence();
    }


    //=================================
    //           METHODS
    // ================================

    /**
     * Method used to add a single secouriste to the graph
     *
     * @param secouriste the secouriste object to add
     * @throws IllegalArgumentException if the secouriste is null
     */
    public void addSecouriste(Secouriste secouriste) throws IllegalArgumentException {
        if (secouriste == null) {
            throw new IllegalArgumentException("Secouriste null");
        }
        this.secouristes.add(secouriste);
    }

    /**
     * Method used to add multiple secouristes to the graph
     *
     * @param secouristes the ArrayList of the secouristes to add
     * @throws IllegalArgumentException if the list is null
     */
    public void addSecouristes(ArrayList<Secouriste> secouristes) throws IllegalArgumentException {
        if (secouristes == null || secouristes.contains(null)) {
            throw new IllegalArgumentException("Secouristes null");
        }
        this.secouristes.addAll(secouristes);
    }

    /**
     * Method used to add a single DPS to the graph
     *
     * @param dps the DPS to add
     * @throws IllegalArgumentException if the DPS is null
     */
    public void addDPS(DPS dps) throws IllegalArgumentException {
        if (dps == null) {
            throw new IllegalArgumentException("DPS null");
        }
        for (Competence c : dps.getCompetences()) {
            this.DPSCompet.add(new Pair<>(dps, c));
        }
    }

    /**
     * Method used to add multiple DPS to the graph
     *
     * @param dpss the ArrayList of the DPS to add
     * @throws IllegalArgumentException if the list is null
     */
    public void addSeveralDPS(ArrayList<DPS> dpss) throws IllegalArgumentException {
        if (dpss == null || dpss.contains(null)) {
            throw new IllegalArgumentException("DPS null");
        }
        for (DPS dps : dpss) {
            for (Competence c : dps.getCompetences()) {
                this.DPSCompet.add(new Pair<>(dps, c));
            }
        }
    }

    /**
     * Method used to initialize the adjacency matrix
     * It's made in a way to always generate a square matrix,
     * to ensure proper function of the subsequent algorithms
     */
    public void initMatriceAdjacence() {
        int n = this.secouristes.size();
        int m = this.DPSCompet.size();
        this.nbSommets = n + m;

        this.matriceAdj = new int[nbSommets][nbSommets];

        for (int i = 0; i < n; i++) {
            Secouriste s = this.secouristes.get(i);
            for (int j = 0; j < m; j++) {
                Pair<DPS, Competence> p = this.DPSCompet.get(j);
                if (s.getCompetences().contains(p.getValue())) {
                    matriceAdj[i][n + j] = 1;
                }
            }
        }
    }

    /**
     * This method checks if the graph is a DAG using Depth First Search
     * It calls the recursive method hasCycle()
     * It's existence is necessary to ensure proper function in
     * case of a disconnected graph ( if not, calling the recursive method would have been enough )
     * @return true if the graph is a DAG, false otherwise
     */
    public boolean DAGCheckDFS() {
        Set<Competence> visited = new HashSet<>();
        Set<Competence> recursionStack = new HashSet<>();
        CompetenceDAO daoComp = new CompetenceDAO();
        boolean isDAG = true;
        
        for (Competence comp : daoComp.findAll()) {
            if (!visited.contains(comp) && hasCycle(comp, visited, recursionStack)) {
                isDAG = false;
            }
        }
        
        return isDAG;
    }

    /**
     * Recursive method used to check if the graph is a DAG
     * It follows the DFS algorithm, going to the end of the current branch before returning true.
     * If it encounters a node already in it's stack, it returns false
     * @param current the current node we're processing
     * @param visited the nodes we've completely processed
     * @param recursionStack the nodes in the current DFS path
     * @return true if the graph is a DAG, false otherwise
     */
    private boolean hasCycle(Competence current, Set<Competence> visited, Set<Competence> recursionStack) {
        visited.add(current);
        recursionStack.add(current);
        boolean cycleFound = false;
        Competence[] requis = current.getRequis().toArray(new Competence[0]);
        int i = 0;

        while (i < requis.length && !cycleFound) {
            Competence req = requis[i];
            boolean isUnvisited = !visited.contains(req);
            boolean isInCurrentPath = recursionStack.contains(req);
            
            if (isUnvisited) {
                cycleFound = hasCycle(req, visited, recursionStack);
            }
            if (!isUnvisited && isInCurrentPath) {
                cycleFound = true;
            }
            i++;
        }

        recursionStack.remove(current);
        return cycleFound;
    }



    //=================================
    //           EXHAUSTIF
    // ================================

    /**
     * Exhaustive algorithm affecting secourists to DPSs
     * @return The list of affectations
     */
    public ArrayList<Affectation> affectationExhaustive() {
        ArrayList<Affectation> resultats = new ArrayList<>();
        
        for (DPS dps : cloneListDPS()) {
            ArrayList<Pair<Secouriste, Competence>> affectationsDPS = new ArrayList<>();
            
            for (Competence competence : dps.getCompetences()) {
                List<Secouriste> candidats = secouristesDisponibles(dps, competence);
                
                if (!candidats.isEmpty()) {
                    Secouriste secouriste = candidats.get(0);
                    
                    affectationsDPS.add(new Pair<>(secouriste, competence));
                    
                    mettreAJourDisponibilites(secouriste, dps);
                }
            }
            
            if (!affectationsDPS.isEmpty()) {
                resultats.add(new Affectation(affectationsDPS, dps));
            }
        }
        
        return resultats;
    }

    /**
     * Returns a copy of the set of DPS without (without any duplicate)
     * @return the copy
     */
    private Set<DPS> cloneListDPS() {
        Set<DPS> cloneDPS = new HashSet<>();
        for (Pair<DPS, Competence> pair : DPSCompet) {
            DPS dps = pair.getKey();
            DPS copy = new DPS(dps.getId(), dps.getHoraireDepart(), dps.getHoraireFin(), dps.getDateEvt(), dps.getCompetences(), 
                        dps.getLieu(), dps.getSport());
            cloneDPS.add(copy);
        }
        return cloneDPS;
    }

    /**
     * Returns the list of secourists available for a DPS's competence
     * @return the list of secourists
     */
    private List<Secouriste> secouristesDisponibles(DPS dps, Competence competence) {
        List<Secouriste> disponibles = new ArrayList<>();
        
        for (Secouriste secouriste : this.secouristes) {
            boolean hasCompetence = secouriste.getCompetences().contains(competence);
            boolean isAvailable = hasCompetence && estDisponible(secouriste, dps);
            
            if (isAvailable) {
                disponibles.add(secouriste);
            }
        }
        
        return disponibles;
    }

    /**
     * Checks if a secourist is available for a DPS
     * @param secouriste the secourist
     * @param dps the dps
     */
    private boolean estDisponible(Secouriste secouriste, DPS dps) {
        LocalDateTime debutDPS = dps.debutToLocalDateTime();
        LocalDateTime finDPS = dps.finToLocalDateTime();
        boolean disponible = false;
        
        for (Dispos dispo : secouriste.getDisponibilites()) {
            LocalDateTime debutDispo = dispo.debutToLocalDateTime();
            LocalDateTime finDispo = dispo.finToLocalDateTime();
            
            boolean memeJour = debutDPS.toLocalDate().equals(debutDispo.toLocalDate());
            boolean couvreDebut = debutDispo.isBefore(debutDPS) || debutDispo.equals(debutDPS);
            boolean couvreFin = finDispo.isAfter(finDPS) || finDispo.equals(finDPS);
            boolean disponibiliteValide = memeJour && couvreDebut && couvreFin;
            
            if (disponibiliteValide) {
                disponible = true;
            }
        }
        
        return disponible;
    }

    /**
     * Updates a secouriste's disponibilities
     * @param secouriste the sec
     * @param dps the dps
     */
    private void mettreAJourDisponibilites(Secouriste secouriste, DPS dps) {
        LocalDateTime debutDPS = dps.debutToLocalDateTime();
        LocalDateTime finDPS = dps.finToLocalDateTime();
        HashSet<Dispos> nouvellesDispos = new HashSet<>();

        for (Dispos dispo : secouriste.getDisponibilites()) {
            LocalDateTime debutDispo = dispo.debutToLocalDateTime();
            LocalDateTime finDispo = dispo.finToLocalDateTime();
            boolean memeJour = debutDPS.toLocalDate().equals(debutDispo.toLocalDate());
            boolean couvreDPS = memeJour && (debutDispo.isBefore(debutDPS) || debutDispo.equals(debutDPS)) && 
                            (finDispo.isAfter(finDPS) || finDispo.equals(finDPS));

            if (memeJour && couvreDPS) {
                gererCreneauxExclusDPS(secouriste, nouvellesDispos, debutDPS, finDPS, debutDispo, finDispo);
            } else {
                nouvellesDispos.add(dispo);
            }
        }
        
        secouriste.setDisponibilites(nouvellesDispos);
    }

    /**
     * Handles dispos before and after an affectation
     * @param secouriste the sec
     * @param nouvellesDispos the new dispos
     * @param debutDPS the start time of the DPS
     * @param finDPS the end time of the DPS
     * @param debutDispo the start time of the dispo
     * @param finDispo the end time of the dispo
     */
    private void gererCreneauxExclusDPS(Secouriste secouriste, HashSet<Dispos> nouvellesDispos, LocalDateTime debutDPS,
                                        LocalDateTime finDPS, LocalDateTime debutDispo, LocalDateTime finDispo) {

        long diffAvant = java.time.Duration.between(debutDispo, debutDPS).toHours();
        boolean ajouterAvant = diffAvant >= 1;
        
        long diffApres = java.time.Duration.between(finDPS, finDispo).toHours();
        boolean ajouterApres = diffApres >= 1;

        if (ajouterAvant) {
            nouvellesDispos.add(new Dispos(secouriste, debutDispo.toLocalDate(), debutDispo.toLocalTime(), debutDPS.toLocalTime()));
        }

        if (ajouterApres) {
            nouvellesDispos.add(new Dispos(secouriste, finDPS.toLocalDate(), finDPS.toLocalTime(), finDispo.toLocalTime()));
        }
    }



    //===============================
    //           GLOUTON
    // ==============================

    /**
     * Greedy algorithm affecting secourists to DPSs
     * @return the list of affectations
     */
    public ArrayList<Affectation> affectationGlouton() {
        ArrayList<Affectation> resultats = new ArrayList<>();
        List<DPS> dpsTries = trierDpsParCompetences();
        
        for (DPS dps : dpsTries) {
            ArrayList<Pair<Secouriste, Competence>> affectationsDPS = new ArrayList<>();
            Set<Secouriste> secouristesDejaAffectes = new HashSet<>();
            List<Competence> competencesTriees = trierCompetencesParRarete(dps);
            
            for (Competence competence : competencesTriees) {
                Secouriste meilleurSecouriste = trouverMeilleurSecouriste(dps, competence, secouristesDejaAffectes, resultats);
                
                if (meilleurSecouriste != null) {
                    affectationsDPS.add(new Pair<>(meilleurSecouriste, competence));
                    secouristesDejaAffectes.add(meilleurSecouriste);
                    mettreAJourDisponibilites(meilleurSecouriste, dps);
                }
            }
            
            if (!affectationsDPS.isEmpty()) {
                resultats.add(new Affectation(affectationsDPS, dps));
            }
        }
        
        return resultats;
    }

    /**
     * Sorts the DPS per required Competence's sets sizes
     * @return the list of sorted DPSs
     */
    private List<DPS> trierDpsParCompetences() {
        List<DPS> dpsTries = new ArrayList<>(cloneListDPS());
        boolean swapped;
        do {
            swapped = false;
            for (int i = 0; i < dpsTries.size() - 1; i++) {
                DPS d1 = dpsTries.get(i);
                DPS d2 = dpsTries.get(i + 1);
                if (d1.getCompetences().size() < d2.getCompetences().size()) {
                    dpsTries.set(i, d2);
                    dpsTries.set(i + 1, d1);
                    swapped = true;
                }
            }
        } while (swapped);
        return dpsTries;
    }

    /**
     * Sorts the Competences per rarity
     * @param dps the dps
     * @return the list of sorted competences
     */
    private List<Competence> trierCompetencesParRarete(DPS dps) {
        List<Competence> competencesTriees = new ArrayList<>(dps.getCompetences());
        
        boolean swapped;
        do {
            swapped = false;
            for (int i = 0; i < competencesTriees.size() - 1; i++) {
                Competence c1 = competencesTriees.get(i);
                Competence c2 = competencesTriees.get(i + 1);
                
                int count1 = countSecouristesAvecCompetence(c1);
                int count2 = countSecouristesAvecCompetence(c2);
                
                if (count1 > count2) {
                    competencesTriees.set(i, c2);
                    competencesTriees.set(i + 1, c1);
                    swapped = true;
                }
            }
        } while (swapped);
        
        return competencesTriees;
    }

    /**
     * Returns the most appropriate secourist for the affectation
     * @param dps the dps
     * @param competence the competence
     * @param secouristesDejaAffectes the already affected secourists
     * @param resultats the list of affectations
     * @return the most appropriate secourist
     */
    private Secouriste trouverMeilleurSecouriste(DPS dps, Competence competence, Set<Secouriste> secouristesDejaAffectes, 
                                                ArrayList<Affectation> resultats) {
        Secouriste meilleurSecouriste = null;
        int minAffectations = Integer.MAX_VALUE;
        
        for (Secouriste secouriste : secouristes) {
            boolean estEligible = !secouristesDejaAffectes.contains(secouriste) && secouriste.getCompetences().contains(competence)
                                && estDisponible(secouriste, dps);
            
            if (estEligible) {
                int nbAffectations = getNombreAffectations(secouriste, resultats);
                if (nbAffectations < minAffectations) {
                    meilleurSecouriste = secouriste;
                    minAffectations = nbAffectations;
                }
            }
        }
        
        return meilleurSecouriste;
    }

    /**
     * Returns the amount of secourists with a specified competence
     * @param comp the competence
     * @return the amount
     */
    private int countSecouristesAvecCompetence(Competence comp) {
        int count = 0;
        for (Secouriste secouriste : secouristes) {
            if (secouriste.getCompetences().contains(comp)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the amount of affectations for a secourist
     * @param sec the secourist
     * @param affectations the general affectations
     * @return the amount
     */
    private int getNombreAffectations(Secouriste sec, List<Affectation> affectations) {
        int total = 0;
        
        for (Affectation affectation : affectations) {
            ArrayList<Pair<Secouriste, Competence>> listePairs = affectation.getList();
            for (Pair<Secouriste, Competence> pair : listePairs) {
                if (pair.getKey().equals(sec)) {
                    total++;
                }
            }
        }
        
        return total;
    }
}
