package model.data.graphe;

import model.data.persistence.*;
import model.dao.CompetenceDAO;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

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
     * Returns the set of DPS without (without any duplicate)
     * @return the set
     */
    private Set<DPS> cloneListDPS() {
        Set<DPS> cloneDPS = new HashSet<>();
        for (Pair<DPS, Competence> pair : DPSCompet) {
            cloneDPS.add(pair.getKey());
        }
        return cloneDPS;
    }

    /**
     * Returns the list of secourists available for a DPS's competence
     * @return the list of secourists
     */
    private List<Secouriste> secouristesDisponibles(DPS dps, Competence competence) {
        List<Secouriste> disponibles = new ArrayList<>();
        
        for (Secouriste secouriste : secouristes) {
            boolean hasCompetence = secouriste.getCompetences().contains(competence);
            boolean isAvailable = hasCompetence && estDisponible(secouriste, dps);
            
            if (isAvailable) {
                disponibles.add(secouriste);
            }
        }
        
        return disponibles;
    }

    /**
     * Vérifie si un secouriste est disponible pour un DPS donné
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
     * Met à jour les disponibilités du secouriste après affectation
     */
    private void mettreAJourDisponibilites(Secouriste secouriste, DPS dps) {
        LocalDateTime debutDPS = dps.debutToLocalDateTime();
        LocalDateTime finDPS = dps.finToLocalDateTime();
        
        HashSet<Dispos> nouvellesDispos = new HashSet<>();
        
        for (Dispos dispo : secouriste.getDisponibilites()) {
            LocalDateTime debutDispo = dispo.debutToLocalDateTime();
            LocalDateTime finDispo = dispo.finToLocalDateTime();
            
            // Vérifie si c'est la même journée
            if (!debutDPS.toLocalDate().equals(debutDispo.toLocalDate())) {
                nouvellesDispos.add(dispo);
                continue;
            }
            
            // Si la disponibilité couvre le DPS
            if ((debutDispo.isBefore(debutDPS) || debutDispo.equals(debutDPS)) && 
                ((finDispo.isAfter(finDPS)) || finDispo.equals(finDPS))) {
                
                // Créer de nouvelles disponibilités si le temps restant est >= 1h
                
                // Temps avant le DPS
                long diffAvant = java.time.Duration.between(debutDispo, debutDPS).toHours();
                if (diffAvant >= 1) {
                    nouvellesDispos.add(new Dispos(
                        secouriste, 
                        debutDispo.toLocalDate(), 
                        debutDispo.toLocalTime(), 
                        debutDPS.toLocalTime()
                    ));
                }
                
                // Temps après le DPS
                long diffApres = java.time.Duration.between(finDPS, finDispo).toHours();
                if (diffApres >= 1) {
                    nouvellesDispos.add(new Dispos(
                        secouriste, 
                        finDPS.toLocalDate(), 
                        finDPS.toLocalTime(), 
                        finDispo.toLocalTime()
                    ));
                }
            } else {
                // Garder la disponibilité si elle ne couvre pas le DPS
                nouvellesDispos.add(dispo);
            }
        }
        
        // Mettre à jour les disponibilités du secouriste
        secouriste.setDisponibilites(nouvellesDispos);
    }


    public ArrayList<Affectation> affectationGlouton() {
        ArrayList<Affectation> resultats = new ArrayList<>();
        
        // Tri des DPS par ordre de difficulté (ceux avec le plus de compétences requises d'abord)
        List<DPS> dpsTries = cloneListDPS().stream()
            .sorted((d1, d2) -> Integer.compare(d2.getCompetences().size(), d1.getCompetences().size()))
            .collect(Collectors.toList());
        
        for (DPS dps : dpsTries) {
            ArrayList<Pair<Secouriste, Competence>> affectationsDPS = new ArrayList<>();
            Set<Secouriste> secouristesDejaAffectes = new HashSet<>();
            
            // Tri des compétences par rareté (compétences possédées par le moins de secouristes d'abord)
            List<Competence> competencesTriees = dps.getCompetences().stream()
                .sorted((c1, c2) -> Integer.compare(
                    countSecouristesAvecCompetence(c1),
                    countSecouristesAvecCompetence(c2)))
                .collect(Collectors.toList());
            
            for (Competence competence : competencesTriees) {
                // Trouve le secouriste disponible avec le moins d'affectations
                Optional<Secouriste> secouristeOpt = secouristes.stream()
                    .filter(s -> !secouristesDejaAffectes.contains(s))
                    .filter(s -> s.getCompetences().contains(competence))
                    .filter(s -> estDisponible(s, dps))
                    .min(Comparator.comparingInt(s -> getNombreAffectations(s, resultats)));
                
                if (secouristeOpt.isPresent()) {
                    Secouriste secouriste = secouristeOpt.get();
                    affectationsDPS.add(new Pair<>(secouriste, competence));
                    secouristesDejaAffectes.add(secouriste);
                    mettreAJourDisponibilites(secouriste, dps);
                }
            }
            
            if (!affectationsDPS.isEmpty()) {
                resultats.add(new Affectation(affectationsDPS, dps));
            }
        }
        
        return resultats;
    }

    // Méthodes utilitaires nécessaires
    private int countSecouristesAvecCompetence(Competence comp) {
        return (int) secouristes.stream()
            .filter(s -> s.getCompetences().contains(comp))
            .count();
    }

    private int getNombreAffectations(Secouriste sec, List<Affectation> affectations) {
        return affectations.stream()
            .mapToInt(a -> (int) a.getList().stream()
                .filter(p -> p.getKey().equals(sec))
                .count())
            .sum();
    }

    /**
     * Checks if a secourist is available for a DPS and if so, changes their availibility in accordance
     * @param sec the secourist
     * @param dps the dps
     * @return true if the secourist is available
     */
    public boolean checkDispos(Secouriste sec, DPS dps){
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
