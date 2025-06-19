package model.data.persistence;

import java.util.ArrayList;
import javafx.util.Pair;

/**
 * Class defining what an affectation is, a list of secourists and their competences associated to a DPS
 * @author Killian Avril, Bastian Le Ouedec, Enrick Mananjean, Emile Thevenin, Elwan Yvin
 */
public class Affectation {

    //=================================
    //           ATTRIBUTES
    // ================================

    /**
     * The list of couples with the competence affected to each secourist
     */
    private ArrayList<Pair<Secouriste, Competence>> listSecComp;

    /**
     * The event
     */
    private DPS idDps;


    //=================================
    //           CONSTRUCTORS
    // ================================

    /**
     * Constructor
     * @param list the list
     * @param idDpsEntrant the event
     * @throws IllegalArgumentException if a parameter is invalid
     */
    public Affectation(ArrayList<Pair<Secouriste, Competence>> list, DPS idDpsEntrant) throws IllegalArgumentException{
        if (list == null){
            throw new IllegalArgumentException("Affectation : paramètres invalides.");
        }
        if (list.isEmpty()){
            throw new IllegalArgumentException("Affectation : liste vide.");
        }
        if (list.contains(null)){
            throw new IllegalArgumentException("Affectation : liste contient du vide.");
        }
        if (idDpsEntrant == null){
            throw new IllegalArgumentException("Affectation : DPS null.");
        }

        this.listSecComp = cloneList(list);
        this.idDps = new DPS(idDpsEntrant.getId(), idDpsEntrant.getHoraireDepart(), idDpsEntrant.getHoraireFin(), 
                            idDpsEntrant.getDateEvt(), idDpsEntrant.getCompetences(), idDpsEntrant.getLieu(), 
                            idDpsEntrant.getSport());
    }


    //=================================
    //           GETTERS
    // ================================

    /**
     * Returns the list
     * @return the list as an ArrayList
     */
    public ArrayList<Pair<Secouriste, Competence>> getList(){
        ArrayList<Pair<Secouriste, Competence>> newList = cloneList(this.listSecComp);

        return newList;
    }

    /**
     * Returns the DPS
     * @return the DPS
     */
    public DPS getDps(){
        return new DPS(this.idDps.getId(), this.idDps.getHoraireDepart(), this.idDps.getHoraireFin(), 
                            this.idDps.getDateEvt(), this.idDps.getCompetences(), this.idDps.getLieu(), 
                            this.idDps.getSport());
    }


    //=================================
    //           SETTERS
    // ================================

    /**
     * Changes the current list for a new list
     * @param newList the new list
     */
    public void setList(ArrayList<Pair<Secouriste, Competence>> newList){
        this.listSecComp = cloneList(newList);
    }

    /**
     * Changes the current DPS for a new DPS
     * @param newIdDps the new DPS
     */
    public void setIdDps(DPS newIdDps){
        if(newIdDps == null){
            throw new IllegalArgumentException("setIdDps : paramètre invalide");
        }
        this.idDps = new DPS(newIdDps.getId(), newIdDps.getHoraireDepart(), newIdDps.getHoraireFin(), 
                            newIdDps.getDateEvt(), newIdDps.getCompetences(), newIdDps.getLieu(), 
                            newIdDps.getSport());
    }


    //=================================
    //           METHODS
    // ================================

    /**
     * Clones the list of pairs
     * @param listOrig the original list
     * @return the new list
     * @throws IllegalArgumentException if a parameter is invalid
     */
    private ArrayList<Pair<Secouriste, Competence>> cloneList(ArrayList<Pair<Secouriste, Competence>> listOrig) throws IllegalArgumentException{
        if (listOrig == null || listOrig.isEmpty() || listOrig.contains(null)){
            throw new IllegalArgumentException("Affectation : paramètres invalides.");
        }

        ArrayList<Pair<Secouriste, Competence>> newList = new ArrayList<>();
        for (Pair<Secouriste, Competence> pair : listOrig) {
            if(pair == null || pair.getKey() == null || pair.getValue() == null || pair.getValue().getIntitule().isEmpty()){
                throw new IllegalArgumentException("Affectation - cloneList : paramètres invalides.");
            }
            Secouriste secOrig = pair.getKey();
            Secouriste cloneSecouriste = new Secouriste(secOrig.getId(), secOrig.getNom(), secOrig.getPrenom(), 
                                                        secOrig.getDateNaissance(), secOrig.getEmail(), secOrig.getTel(), 
                                                        secOrig.getAdresse(), secOrig.getCompetences(), secOrig.getDisponibilites());
            Competence compOrig = pair.getValue();
            Competence cloneCompetence = new Competence(compOrig.getIntitule(), compOrig.getRequis());
            newList.add(new Pair<>(cloneSecouriste, cloneCompetence));
        }

        return newList;
    }
}
