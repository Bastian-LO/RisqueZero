package model.data.persistence;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Class defining a date with a day, a month and a year
 * @author Killian Avril, Bastian Le Ouedec, Enrick Mananjean, Emile Thevenin, Elwan Yvin
 */
public class Journee{

    //=================================
    //           ATTRIBUTES
    // ================================

    /**
     * The day of the month
     */
    private int jour;

    /**
     * The month as a number
     */
    private int mois;

    /**
     * The year
     */
    private int annee;


    //=================================
    //           CONSTRUCTORS
    // ================================

    /**
     * Constructor with integers
     * @param jour the day
     * @param mois the month
     * @param annee the year
     * @throws IllegalArgumentException if a parameter is invalid
     */
    public Journee(int jour, int mois, int annee) throws IllegalArgumentException{
        checkDate(jour, mois, annee);

        this.jour = jour;
        this.mois = mois;
        this.annee = annee;
    }

    /**
     * Constructor for creating a Journee with a LocalDate (used in JourneeDAO)
     * @param date the date as a LocalDate
     */
    public Journee(LocalDate date) {
        this(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
    }


    //================================
    //           GETTERS
    //================================

    /**
     * Returns the day of the month as an int
     * @return the day
     */
    public int getJour(){
        return this.jour;
    }

    /**
     * Returns the month as an int
     * @return the month
     */
    public int getMois(){
        return this.mois;
    }

    /**
     * Returns the year
     * @return the year
     */
    public int getAnnee(){
        return this.annee;
    }


    //====================================
    //              SETTERS
    //====================================

    /**
     * Setter for the day
     * @param newJour the new day
     * @throws IllegalArgumentException if the new day is invalid
     */
    public void setJour(int newJour) throws IllegalArgumentException{
        checkDate(newJour, this.mois, this.annee);

        this.jour = newJour;
    }

    /**
     * Setter for the month
     * @param newMois the new month
     * @throws IllegalArgumentException if the new month is invalid
     */
    public void setMois(int newMois) throws IllegalArgumentException{
        checkDate(this.jour, newMois, this.annee);

        this.mois = newMois;
    }

    /**
     * Setter for the year
     * @param newAnnee the new year
     * @throws IllegalAccessException if the new year is invalid
     */
    public void setAnnee(int newAnnee) throws IllegalAccessException{
        checkDate(this.jour, this.mois, newAnnee);

        this.annee = newAnnee;
    }


    //=================================
    //           METHODS
    // ================================

    /**
     * Checks if the date respects the universal conventions
     * @param jour the day
     * @param mois the month
     * @param annee the year
     * @throws IllegalArgumentException if a parameter is invalid
     */
    private void checkDate(int jour, int mois, int annee) throws IllegalArgumentException{
        if (jour < 1 || jour > 31 || mois < 1 || mois > 12 || annee < 0){
            throw new IllegalArgumentException("La date est impossible");
        }

        if (mois == 4 || mois == 6 || mois == 9 || mois == 11){
            if(jour > 30){
                throw new IllegalArgumentException("La date est impossible");
            }
        } else if (mois == 2){  
            // Traitement des années bissextiles
            if(annee % 4 != 0 || (annee % 100 == 0 && annee % 400 != 0)){
                if (jour > 28){
                    throw new IllegalArgumentException("La date est impossible");
                }
            } else {
                if (jour > 29){
                    throw new IllegalArgumentException("La date est impossible");
                }
            }
        } else {
            if (jour > 31){
                throw new IllegalArgumentException("La date est impossible");
            }
        }
    }

    /**
     * Returns a string with the formatted date (dd/mm/yy)
     * @returns the date as a string
     */
    @Override
    public String toString() {
        return String.format("%02d/%02d/%04d", this.jour, this.mois, this.annee); // Format the date as dd/mm/yyyy
    }

    /**
     * Returns the date as a LocalDate object
     * @return the LocalDate object
     */
    public LocalDate toLocalDate() {
        return LocalDate.of(annee, mois, jour);
    }

    /**
     * Checks if the current date is equal to the object in parameter
     * @return true if it's the same date
     */
    @Override
    public boolean equals(Object obj){
        boolean ret = false;

        if (this == obj){
            ret = true;
        } else if (obj instanceof Journee){
            Journee autre = (Journee) obj;
            if(this.getJour() == autre.getJour() && this.getMois() == autre.getMois() && 
            this.getAnnee() == autre.getAnnee()){
                ret = true;
            }
        }
        return ret;
    }

    /**
     * Returns the hashcode of the date
     * @return the hashcode
     */
    @Override
    public int hashCode(){
        int ret;
        ret = Objects.hash(getJour(), getMois(), getAnnee());
        return ret;
    }
}