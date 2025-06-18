package model.data.persistence;

import java.time.LocalDate;
import java.util.Objects;

public class Journee{
    private int jour;
    private int mois;
    private int annee;

    public Journee(int jour, int mois, int annee) throws IllegalArgumentException{
        checkDate(jour, mois, annee);

        this.jour = jour;
        this.mois = mois;
        this.annee = annee;
    }

    /**
     * Constructor for creating a Journee with a LocalDate (used in JourneeDAO)
     * @param date
     */
    public Journee(LocalDate date) {
        this(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
    }

    public int getJour(){
        return this.jour;
    }

    public int getMois(){
        return this.mois;
    }

    public int getAnnee(){
        return this.annee;
    }

    public void setJour(int newJour) throws IllegalArgumentException{
        checkDate(newJour, this.mois, this.annee);

        this.jour = newJour;
    }

    public void setMois(int newMois) throws IllegalArgumentException{
        checkDate(this.jour, newMois, this.annee);

        this.mois = newMois;
    }

    public void setAnnee(int newAnnee) throws IllegalAccessException{
        checkDate(this.jour, this.mois, newAnnee);

        this.annee = newAnnee;
    }

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

    @Override
    public String toString() {
        return String.format("%02d/%02d/%04d", this.jour, this.mois, this.annee); // Format the date as dd/mm/yyyy
    }

    
    public LocalDate toLocalDate() {
        return LocalDate.of(annee, mois, jour);
    }

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

    @Override
    public int hashCode(){
        int ret;
        ret = Objects.hash(getJour(), getMois(), getAnnee());
        return ret;
    }
}