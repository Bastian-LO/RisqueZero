package model.data.graphe;

import model.data.persistence.*;
import model.dao.CompetenceDAO;

import java.rmi.UnexpectedException;
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
    public boolean DAGCheckDFS(){
        Set<Competence> visited = new HashSet<>();
        Set<Competence> recursionStack = new HashSet<>();
        CompetenceDAO DAOcomp = new CompetenceDAO();
        for (Competence comp : DAOcomp.findAll()) {
            if (!visited.contains(comp)) {
                if (hasCycle(comp, visited, recursionStack)) {
                    return false;
                }
            }
        }
        return true;
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
    private boolean hasCycle(Competence current,
                             Set<Competence> visited,
                             Set<Competence> recursionStack) {
        visited.add(current);
        recursionStack.add(current);

        for (Competence req : current.getRequis()) {
            if (!visited.contains(req)) {
                if (hasCycle(req, visited, recursionStack)) {
                    return true;
                }
            }
            // If we find a node already in the stack, we have encountered a cycle.
            else if (recursionStack.contains(req)) {
                return true;
            }
        }

        // Clears the recursion stack as we finish the recursion
        recursionStack.remove(current);
        return false;
    }

    public ArrayList<Affectation> exhaustif(ArrayList<Secouriste> listeSec) {
        ArrayList<Affectation> ret = new ArrayList<>();
        ArrayList<Pair<Pair<DPS, Competence>, Secouriste>> tripleMonstreAffec = new ArrayList<>();
        for (Pair<DPS, Competence> pair : this.DPSCompet) {   // On parcourt toutes les paires DPS / Competence
            DPS dpsCurr = pair.getKey();    // DPS analysé
            Competence compCurr = pair.getValue();  // Compétence requise

            boolean finnishFlag;
            do {
                finnishFlag = true;
                for (int i = 0; i < listeSec.size(); i++) {   // On parcourt tous les secouristes
                    Secouriste secCurr = listeSec.get(i);
                    if (secCurr.getCompetencesIntitules().contains(compCurr.getIntitule())
                            && checkDispos(secCurr, dpsCurr)) {
                        tripleMonstreAffec.add(new Pair<>(new Pair<>(dpsCurr, compCurr), secCurr));
                        // enlever la dispo
                        if (finnishFlag) {
                            finnishFlag = false;
                        }
                    }
                }
            } while (!finnishFlag);
        }
        LinkedHashSet<DPS> listDPS = new LinkedHashSet<>();
        for (Pair<DPS, Competence> p : this.DPSCompet) {
            listDPS.add(p.getKey());
        }
        for (DPS dps : listDPS) {
            ArrayList<Pair<Secouriste, Competence>> listePairs = new ArrayList<>();
            for (int i = 0; i < tripleMonstreAffec.size(); i++) {
                Pair<Pair<DPS, Competence>, Secouriste> monstreCurr = tripleMonstreAffec.get(i);
                if (monstreCurr.getKey().getKey().equals(dps)) {
                    Pair<Secouriste, Competence> p = new Pair<>(monstreCurr.getValue(), monstreCurr.getKey().getValue());
                    tripleMonstreAffec.remove(monstreCurr);
                    listePairs.add(p);
                }
            }
            Affectation affCurr = new Affectation(listePairs, dps);
            ret.add(affCurr);
        }

        return ret;
    }

    /**
     * Checks if a secourist is available for a DPS and if so, changes their availibility in accordance
     * @param sec the secourist
     * @param dps the dps
     * @return true if the secourist is available
     * @throws UnexpectedException thrown if somehow somethings goes out of bounds
     */
    private boolean checkDispos(Secouriste sec, DPS dps) throws UnexpectedException{
        boolean ret = false;
        for (Dispos dispo : sec.getDisponibilites()){   // On parcourt toutes les disponibilités du secouriste
            if(!ret){
                boolean memeJour = dispo.getDate().equals(dps.getDateEvt());    // Vérifie que les journées correspondent

                LocalTime debutDispo = dispo.toLocalTime(dispo.getHeureDebut());    // L'heure de début de la disponibilité en LocalTime
                LocalTime finDispo = dispo.toLocalTime(dispo.getHeureFin());    // L'heure de fin de la dispo en LocalTime
                LocalTime debutDPS = dps.toLocalTime(dps.getHoraireDepart());   // L'heure de début du DPS en LocalTime
                LocalTime finDPS = dps.toLocalTime(dps.getHoraireFin());        // L'heure de fin du DPS en LocalTime

                boolean horairesInclus = !debutDPS.isBefore(debutDispo) && !finDPS.isAfter(finDispo);   // Vérifie que l'horaire du DPS est inclus dans les dispos du secouriste

                if (memeJour && horairesInclus){    // Si le jour et l'horaire correspond, donc qu'une dispo est trouvée...
                    ret = true;                     // On sort de la boucle après avoir mis à jour les disponibilités du secouriste
                    Duration diffHoraireDebut = Duration.between(debutDispo, debutDPS); // Ecart entre le début du DPS et le début des dispos (0 possible)
                    Duration diffHoraireFin = Duration.between(finDispo, finDPS);     // Ecart entre le fin du DPS et le fin des dispos (0 possible)
                    sec.getDisponibilites().remove(dispo);  // Une disponibilité étant trouvée, on la supprime des dispos du secouriste

                    if(debutDispo.isAfter(debutDPS) || finDispo.isBefore(finDPS)){       // S'il y a une erreur à la sélection de la dispo, on lance une exception
                        throw new UnexpectedException("checkDispos : erreur inattendue");
                    }

                    if(diffHoraireDebut.getSeconds() >= 3600){                              // Si le temps précédant le DPS est supérieur à 1h...
                        LocalTime firstNewHoraireFin = finDispo.minus(diffHoraireDebut);
                        Dispos firstNewDispo = new Dispos(sec, dispo.getDate().toLocalDate(), debutDispo, firstNewHoraireFin);
                        sec.getDisponibilites().add(firstNewDispo);                         // On rajoute la disponibilité précédant le DPS
                    }

                    if (diffHoraireFin.getSeconds() >= 3600){                               // Si le temps suivant le DPS est supérieur à 1h...
                        LocalTime secondNewHoraireDebut = finDispo.minus(diffHoraireFin);
                        Dispos secondNewDispo = new Dispos(sec, dispo.getDate().toLocalDate(), secondNewHoraireDebut, finDispo);
                        sec.getDisponibilites().add(secondNewDispo);                        // On rajoute la disponibilité suivant le DPS
                    } 
                }
            }
        }
        return ret;
    }


    /**
     * Returns a HashMap with the amount of Competence required per DPS
     *
     * @return the map
     */
    private HashMap<DPS, Integer> getNbComp(){
        HashMap<DPS, Integer> ret = new HashMap<>();

        for (int i = 0; i < this.DPSCompet.size(); i++) {
            Pair<DPS, Competence> pair = this.DPSCompet.get(i);

            if (!ret.containsKey(pair.getKey())){
                ret.put(pair.getKey(), 1);
            } else {
                int valueCurr = ret.get(pair.getKey()) + 1;
                ret.replace(pair.getKey(), valueCurr);
            }
        }

        return ret;
    }
}
