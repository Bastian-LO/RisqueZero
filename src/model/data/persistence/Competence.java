package model.data.persistence;

import java.util.HashSet;

/**
 * Class representing a competence of a secouriste
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */
public class Competence {

    //=================================
    //           ATTRIBUTES
    // ================================

    /** Name of the competence */
    private final String intitule;

    /** Requisites of the competence (other competences), size should be <= 9 (only 9 competences) */
    private HashSet<Competence> requis;


    //=================================
    //           CONSTRUCTORS
    // ================================

    /** 
     * Constructor of a competence with only the name 
     * Initialize the requis with an empty set
     * @param intitule name of the competence
     * @throws IllegalArgumentException if the name is invalid (null or empty)
     */
    public Competence(String intitule) throws IllegalArgumentException {
        if (intitule == null || intitule.trim().equals("")){
            throw new IllegalArgumentException("Compétence : paramètre invalide.");
        }
        this.intitule = intitule;
        this.requis = new HashSet<>();
    }

    /** 
     * Constructor of a competence with the name and the requisites
     * @param intitule name of the competence
     * @param requis requisites of the competence
     * @throws IllegalArgumentException if the name or the requisites are invalid (null or empty, or size > 9 for the requisites)
     */
    public Competence (String intitule, HashSet<Competence> requis) throws IllegalArgumentException {
        if (intitule == null || intitule.trim().equals("")
                || requis == null || requis.size() > 9){
            throw new IllegalArgumentException("Compétence : paramètres invalides.");
        }
        this.intitule = intitule;
        this.requis = (HashSet<Competence>) requis.clone();
    }


    //=================================
    //           GETTERS
    // ================================

    /** 
     * Returns the name of the competence
     * @return name of the competence
     */
    public String getIntitule() {
        return intitule;
    }

    /** 
     * Returns the requisites of the competence
     * @return requisites of the competence
     */
    public HashSet<Competence> getRequis() {
        return (HashSet<Competence>) requis.clone();
    }


    //=================================
    //           SETTERS
    // ================================

    /** 
     * Sets the requisites of the competence
     * @param requis requisites of the competence
     * @throws IllegalArgumentException if the requisites are invalid (null or size > 9)
     */
    public void setRequis(HashSet<Competence> requis) throws IllegalArgumentException {
        if (requis == null || requis.size() > 9){
            throw new IllegalArgumentException("Compétence.setRequis : paramètre invalide.");
        }
        this.requis = (HashSet<Competence>) requis.clone();
    }

    
    //=================================
    //           METHODS
    // ================================

    /**
     * Returns the name of the competence
     * @return the name of the competence
     */
    public String toString(){
        return intitule;
    }
}