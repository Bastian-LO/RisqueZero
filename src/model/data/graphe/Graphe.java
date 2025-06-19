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

    /**
     * Method returning the best configuration of secourists for the DPS
     * @return the best configuration of secourists for the DPS
     */
    public ArrayList<Affectation> startExhaustif() {
        ArrayList<Affectation> bestSolution = new ArrayList<>();
        ArrayList<Secouriste> clones = deepCloneSecouristes(this.secouristes);
        generatePermutations(clones, 0, bestSolution);
        return bestSolution;
    }


    private void generatePermutations(ArrayList<Secouriste> arr, int index, ArrayList<Affectation> bestSolution) {
        if (index == arr.size()) {
            ArrayList<Secouriste> permutationClone = deepCloneSecouristes(arr);
            ArrayList<Affectation> current = affectationExhaustive(permutationClone);
            if (current.size() > bestSolution.size()) {
                bestSolution.clear();
                bestSolution.addAll(current);
            }
            return;
        }
        for (int i = index; i < arr.size(); i++) {
            Collections.swap(arr, index, i);
            generatePermutations(arr, index + 1, bestSolution);
            Collections.swap(arr, index, i);
        }
    }

    private ArrayList<Secouriste> deepCloneSecouristes(ArrayList<Secouriste> secouristes) {
        ArrayList<Secouriste> clones = new ArrayList<>();
        for (Secouriste sec : secouristes) {
            Secouriste clone = new Secouriste(
                    sec.getId(), sec.getNom(), sec.getPrenom(),
                    sec.getDateNaissance(), sec.getEmail(), sec.getTel(),
                    sec.getAdresse(), new ArrayList<>(sec.getCompetences()),
                    new HashSet<>()
            );
            for (Dispos d : sec.getDisponibilites()) {
                clone.addDispos(new Dispos(clone, d.getDate(), d.getHeureDebut(), d.getHeureFin()));
            }
            clones.add(clone);
        }
        return clones;
    }

    private ArrayList<Affectation> affectationExhaustive(ArrayList<Secouriste> secouristes) {
        // Structure pour stocker les affectations par DPS
        Map<DPS, Map<Secouriste, Competence>> affectationsMap = new HashMap<>();

        for (Pair<DPS, Competence> poste : DPSCompet) {
            DPS dps = poste.getKey();
            Competence competence = poste.getValue();

            for (Secouriste sec : secouristes) {
                if (sec.getCompetences().contains(competence)
                        && reserveCreneau(sec, dps)) {

                    affectationsMap
                            .computeIfAbsent(dps, k -> new HashMap<>())
                            .put(sec, competence);
                    break;
                }
            }
        }

        // Conversion en liste d'affectations
        ArrayList<Affectation> result = new ArrayList<>();
        for (Map.Entry<DPS, Map<Secouriste, Competence>> entry : affectationsMap.entrySet()) {
            ArrayList<Pair<Secouriste, Competence>> pairs = new ArrayList<>();
            entry.getValue().forEach((sec, comp) -> pairs.add(new Pair<>(sec, comp)));
            result.add(new Affectation(pairs, entry.getKey()));
        }
        return result;
    }

    private boolean reserveCreneau(Secouriste sec, DPS dps) {
        for (Iterator<Dispos> it = sec.getDisponibilites().iterator(); it.hasNext();) {
            Dispos dispo = it.next();
            if (dispo.getDate().equals(dps.getDateEvt())) {
                LocalTime debutDispo = dispo.toLocalTime(dispo.getHeureDebut());
                LocalTime finDispo = dispo.toLocalTime(dispo.getHeureFin());
                LocalTime debutDPS = dps.toLocalTime(dps.getHoraireDepart());
                LocalTime finDPS = dps.toLocalTime(dps.getHoraireFin());

                if (!debutDPS.isBefore(debutDispo) && !finDPS.isAfter(finDispo)) {
                    it.remove(); // Supprime le créneau entier

                    // Ajoute les segments restants si besoin
                    if (Duration.between(debutDispo, debutDPS).getSeconds() >= 3600) {
                        sec.addDispos(new Dispos(sec, dispo.getDate().toLocalDate(), debutDispo, debutDPS));
                    }
                    if (Duration.between(finDPS, finDispo).getSeconds() >= 3600) {
                        sec.addDispos(new Dispos(sec, dispo.getDate().toLocalDate(), finDPS, finDispo));
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if a secourist is available for a DPS and if so, changes their availibility in accordance
     * @param sec the secourist
     * @param dps the dps
     * @return true if the secourist is available
     */
    private boolean checkDispos(Secouriste sec, DPS dps){
        boolean ret = false;
        ArrayList<Dispos> newDispos = new ArrayList<>();
        ArrayList<Dispos> aSuppr = new ArrayList<>();

        for (Dispos dispo : sec.getDisponibilites()){   // On parcourt toutes les disponibilités du secouriste
            if(!ret){
                boolean memeJour = dispo.getDate().toLocalDate().equals(dps.getDateEvt().toLocalDate());    // Vérifie que les journées correspondent

                LocalTime debutDispo = dispo.toLocalTime(dispo.getHeureDebut());    // L'heure de début de la disponibilité en LocalTime
                LocalTime finDispo = dispo.toLocalTime(dispo.getHeureFin());        // L'heure de fin de la dispo en LocalTime
                LocalTime debutDPS = dps.toLocalTime(dps.getHoraireDepart());       // L'heure de début du DPS en LocalTime
                LocalTime finDPS = dps.toLocalTime(dps.getHoraireFin());            // L'heure de fin du DPS en LocalTime

                boolean horairesInclus = !debutDPS.isBefore(debutDispo) && !finDPS.isAfter(finDispo);   // Vérifie que l'horaire du DPS est inclus dans les dispos du secouriste

                if (memeJour && horairesInclus){    // Si le jour et l'horaire correspond, donc qu'une dispo est trouvée...
                    ret = true;                     // On sort de la boucle après avoir mis à jour les disponibilités du secouriste
                    Duration diffHoraireDebut = Duration.between(debutDispo, debutDPS); // Ecart entre le début du DPS et le début des dispos (0 possible)
                    Duration diffHoraireFin = Duration.between(finDPS, finDispo);     // Ecart entre le fin du DPS et le fin des dispos (0 possible)
                    aSuppr.add(dispo);  // Une disponibilité étant trouvée, on la supprime des dispos du secouriste

                    if(diffHoraireDebut.getSeconds() >= 3600){                              // Si le temps précédant le DPS est supérieur à 1h...
                        LocalTime firstNewHoraireFin = finDispo.minus(diffHoraireDebut);
                        Dispos firstNewDispo = new Dispos(sec, dispo.getDate().toLocalDate(), debutDispo, firstNewHoraireFin);
                        newDispos.add(firstNewDispo);                         // On rajoute la disponibilité précédant le DPS
                    }

                    if (diffHoraireFin.getSeconds() >= 3600){                               // Si le temps suivant le DPS est supérieur à 1h...
                        LocalTime secondNewHoraireDebut = finDispo.minus(diffHoraireFin);
                        Dispos secondNewDispo = new Dispos(sec, dispo.getDate().toLocalDate(), secondNewHoraireDebut, finDispo);
                        newDispos.add(secondNewDispo);                        // On rajoute la disponibilité suivant le DPS
                    } 
                }
            }
        }

        for (Dispos d : aSuppr) {
            sec.getDisponibilites().remove(d);
        }
        for (Dispos d : newDispos) {
            sec.getDisponibilites().add(d);
        }

        return ret;
    }

    /**
     * Checks if the current affectation list is more optimal than the registered best solution
     * @param current the affectation list we want to check
     * @param bestSolution the best registered affectation list
     * @return true if it's more optimal
     */
    private boolean aIsBetterThanB(ArrayList<Affectation> current, ArrayList<Affectation> bestSolution){
        int nbBestSolution = 0 ;
        int nbCurrent = 0;
        for ( Affectation affectation : bestSolution) {
            nbBestSolution += affectation.getList().size();
        }
        for ( Affectation affectation : current){
            nbCurrent += affectation.getList().size();
        }

        return nbCurrent>nbBestSolution;
    }

    /**
     * Returns a HashMap with the amount of Competence required per DPS
     * @return a map with the amount of competences for each DPS
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

    public ArrayList<Affectation> glouton(){
        ArrayList<Affectation> ret = new ArrayList<>();
        HashMap<DPS, Integer> nbCompParDps = this.getNbComp();  // Pour chaque DPS, le nb de compétences requises
        
        for(Pair<DPS, Competence> pair : this.DPSCompet){   // On parcourt toutes les paires DPS / Competence
            ArrayList<Pair<Secouriste, Competence>> listPair = new ArrayList<>();
            DPS dpsCurr = pair.getKey();    // DPS analysé
            Competence compCurr = pair.getValue();  // Compétence requise
            int nbComp = nbCompParDps.get(pair.getKey());   // Nb de compétences requises pour le DPS

            for(int i = 0; i < this.secouristes.size(); i++){   // On parcourt tous les secouristes
                Secouriste secCurr = this.secouristes.get(i);
                if(secCurr.getCompetencesIntitules().contains(compCurr.getIntitule()) && checkDispos(secCurr, dpsCurr)){
                    Pair<Secouriste, Competence> pairCurr = new Pair<Secouriste,Competence>(secCurr, compCurr);
                    listPair.add(pairCurr);
                }
            }

            Affectation affCurr = new Affectation(listPair, dpsCurr);
            ret.add(affCurr);
        }

        return ret;
    }
}
