package model.data.persistence;

import java.util.ArrayList;

/**
 * Class initializing a security event
 * @author Bastian Le Ouedec, Elwan Yvin, Emile Thevenin, Enrick Mananjean, Killian Avril
 */
public class DPS {

    //====================================
    //            ATTRIBUTES
    //====================================

    /**
     * The id of the event
     */
    private long id;

    /**
     * The beginning hour of the event
     */
    private int[] horaire_depart;

    /**
     * The ending hour of the event
     */
    private int[] horaire_fin;

    /**
     * The day of the event
     */
    private Journee programme;

    /**
     * The list of competences required
     */
    private ArrayList<Competence> competences;

    /**
     * The place where the event takes place
     */
    private Site lieu;

    /**
     * The sport practiced during the event
     */
    private Sport sport;


    //====================================
    //            CONSTRUCTOR
    //====================================

    /**
     * Constructor
     * @param id1 the id
     * @param horaire_depart1 the beginning hour
     * @param horaire_fin1 the ending hour
     * @param programme1 the day
     * @param competences1 the list of required skills
     * @param lieu1 the place
     * @param sport1 the sport
     * @throws IllegalArgumentException true if a parameter is invalid
     */
    public DPS(long id1, int[] horaire_depart1, int[] horaire_fin1, Journee programme1, ArrayList<Competence> competences1, Site lieu1, Sport sport1) throws IllegalArgumentException{
        if (id1 < 0 || horaire_depart1 == null || horaire_fin1 == null || programme1 == null || competences1 == null || 
        lieu1 == null || sport1 == null){
            throw new IllegalArgumentException("Constructeur DPS : paramètres invalides");
        }

        if (horaire_depart1.length != 2 || horaire_fin1.length != 2 || horaire_depart1[0] < 0 || horaire_depart1[1] < 0 || 
        horaire_fin1[0] < 0 || horaire_fin1[1] < 0 || horaire_depart1[0] > 23 || horaire_depart1[1] > 59 || 
        horaire_fin1[0] > 23 || horaire_fin1[1] > 59){
            throw new IllegalArgumentException("Constructeur DPS : paramètres invalides");
        }

        this.id = id1;
        this.horaire_depart = horaire_depart1.clone();
        this.horaire_fin = horaire_fin1.clone();
        this.programme = new Journee(programme1.getJour(), programme1.getMois(), programme1.getAnnee());
        this.competences = (ArrayList<Competence>) competences1.clone();
        this.lieu = new Site(lieu1.getCode(), lieu1.getNom(), lieu1.getLongitude(), lieu1.getLatitude());
        this.sport = new Sport(sport1.getCode(), sport1.getNom());
    }


    //====================================
    //              GETTERS
    //====================================

    /**
     * Getter for the id
     * @return the id
     */
    public long getId(){
        return this.id;
    }

    /**
     * Getter for the beginning hour
     * @return the beginning hour
     */
    public int[] getHoraireDepart(){
        return horaire_depart.clone();
    }

    /**
     * Getter for the ending hour
     * @return the ending hour
     */
    public int[] getHoraireFin(){
        return horaire_fin.clone();
    }

    /**
     * Getter for the day
     * @return the day
     */
    public Journee getProgramme(){
        return new Journee(this.programme.getJour(), this.programme.getMois(), this.programme.getAnnee());
    }

    /**
     * Getter for the list of skills
     * @return the list of skills
     */
    public ArrayList<Competence> getCompetences(){
        return (ArrayList<Competence>) competences.clone();
    }

    /**
     * Getter for the place
     * @return the place
     */
    public Site getLieu(){
        return new Site(this.lieu.getCode(), this.lieu.getNom(), this.lieu.getLongitude(), this.lieu.getLatitude());
    }

    /**
     * Getter for the sport
     * @return the sport
     */
    public Sport getSport(){
        return new Sport(this.sport.getCode(), this.sport.getNom());
    }


    //====================================
    //              SETTERS
    //====================================

    /**
     * Setter for the id
     * @param newId the new id
     * @throws IllegalArgumentException true if the parameter is invalid
     */
    public void setId(long newId) throws IllegalArgumentException{
        if (newId < 0){
            throw new IllegalArgumentException("setId : paramètre invalide");
        }
        this.id = newId;
    }

    /**
     * Setter for the beginning hour
     * @param newHoraire_Depart the new beginning hour
     * @throws IllegalArgumentException true if the parameter is invalid
     */
    public void setHoraireDepart(int[] newHoraire_Depart) throws IllegalArgumentException{
        if(newHoraire_Depart == null || newHoraire_Depart.length != 2 || newHoraire_Depart[0] < 0 || 
        newHoraire_Depart[0] > 23 || newHoraire_Depart[1] < 0 || newHoraire_Depart[1] > 59){
            throw new IllegalArgumentException("setHoraireDepart : paramètre invalide");
        }
        this.horaire_depart = newHoraire_Depart.clone();
    }

    /**
     * Setter for the ending hour
     * @param newHoraire_Depart the new ending hour
     * @throws IllegalArgumentException true if the parameter is invalid
     */
    public void setHoraireFin(int[] newHoraire_Depart) throws IllegalArgumentException{
        if(newHoraire_Depart == null || newHoraire_Depart.length != 2 || newHoraire_Depart[0] < 0 || 
        newHoraire_Depart[0] > 23 || newHoraire_Depart[1] < 0 || newHoraire_Depart[1] > 59){
            throw new IllegalArgumentException("setHoraireDepart : paramètre invalide");
        }
        this.horaire_depart = newHoraire_Depart.clone();
    }

    /**
     * Setter for the day
     * @param newProgramme the new day
     * @throws IllegalArgumentException true if the parameter is invalid
     */
    public void setProgramme(Journee newProgramme) throws IllegalArgumentException{
        if(newProgramme == null){
            throw new IllegalArgumentException("setProgramme : paramètre invalide");
        }
        this.programme = newProgramme;
    }

    /**
     * Setter for the list of skills
     * @param newCompetences the new skills
     * @throws IllegalArgumentException true if the parameter is invalid
     */
    public void setCompetences(ArrayList<Competence> newCompetences) throws IllegalArgumentException{
        if(newCompetences == null){
            throw new IllegalArgumentException("setCompetences : paramètre invalide");
        }
        if (newCompetences.contains(null)){
            throw new IllegalArgumentException("setCompetences : liste contient un élément invalide");
        }

        this.competences = (ArrayList<Competence>) newCompetences.clone();
    }

    /**
     * Setter for the place
     * @param newLieu the new place
     * @throws IllegalArgumentException true if the parameter is invalid
     */
    public void setLieu(Site newLieu) throws IllegalArgumentException{
        if(newLieu == null){
            throw new IllegalArgumentException("setLieu : paramètre invalide");
        }
        this.lieu = new Site(newLieu.getCode(), newLieu.getNom(), newLieu.getLongitude(), newLieu.getLatitude());
    }

    /**
     * Setter for the sport
     * @param newSport the new sport
     * @throws IllegalArgumentException true if the parameter is invalid
     */
    public void setSport(Sport newSport) throws IllegalArgumentException{
        if(newSport == null){
            throw new IllegalArgumentException("setSport : paramètre invalide");
        }
        this.sport = new Sport(newSport.getCode(), newSport.getNom());
    }

}