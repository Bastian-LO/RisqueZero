package model.data.graphe;

import model.data.persistence.*;
import model.dao.CompetenceDAO;

import java.time.LocalDateTime;
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

    public ArrayList<Affectation> exhaustif(){
        ArrayList<Affectation> ret = new ArrayList<>();
        HashMap<DPS, Integer> nbCompParDps = this.getNbComp();  // Pour chaque DPS, le nb de compétences requises
        
        for(Pair<DPS, Competence> pair : this.DPSCompet){   // On parcourt toutes les paires DPS / Competence
            ArrayList<Pair<Secouriste, Competence>> listPair = new ArrayList<>();
            DPS dpsCurr = pair.getKey();    // DPS analysé
            Competence compCurr = pair.getValue();  // Compétence requise
            int nbComp = nbCompParDps.get(pair.getKey());   // Nb de compétences requises pour le DPS

            for(int i = 0; i < this.secouristes.size(); i++){   // On parcourt tous les secouristes
                Secouriste secCurr = this.secouristes.get(i);
                if(secCurr.getCompetencesIntitules().contains(compCurr.getIntitule())){
                    Pair<Secouriste, Competence> pairCurr = new Pair<Secouriste,Competence>(secCurr, compCurr);
                    listPair.add(pairCurr);
                }
            }

            Affectation affCurr = new Affectation(listPair, dpsCurr);
            ret.add(affCurr);
        }

        return ret;
    }

    private boolean checkDispos(Secouriste sec, DPS dps){
        boolean ret = false;
        for (Dispos dispo : sec.getDisponibilites()){
            while(!ret){
                boolean memeJour = dispo.getDate().equals(dps.getDateEvt());

                LocalDateTime debutDispo = dispo.debutToLocalDateTime();
                LocalDateTime finDispo = dispo.finToLocalDateTime();
                LocalDateTime debutDPS = dps.debutToLocalDateTime();
                LocalDateTime finDPS = dps.finToLocalDateTime();

                boolean horairesInclus = !debutDPS.isBefore(debutDispo) && !finDPS.isAfter(finDispo);

                if (memeJour || horairesInclus){
                    ret = true;
                }
            }
        }

        return ret;
    }

    /**
     * Returns a HashMap with the amount of Competence required per DPS
     * @return the map
     */
    private HashMap<DPS, Integer> getNbComp(){
        HashMap<DPS, Integer> ret = new HashMap<>();

        for (int i = 0; i < this.DPSCompet.size(); i++){
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
