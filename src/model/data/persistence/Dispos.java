package model.data.persistence;

import java.time.*;
import java.util.Objects;

/**
 * Class representing the disposibilities of a secouriste
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */
public class Dispos {

    //=================================
    //           ATTRIBUTES
    // ================================

    /** The secouriste */
    private Secouriste secouriste;

    /** The date of the disponibility */
    private Journee date;

    /** The start time of the disponibility */
    private int[] heureDebut;

    /** The end time of the disponibility */
    private int[] heureFin;

    //=================================
    //           CONSTRUCTORS
    // ================================

    public Dispos(Secouriste secouriste, Journee date, int[] heureDebut, int[] heureFin) throws IllegalArgumentException {
        // Checks if the parameters are valid
        if (secouriste == null || date == null || heureDebut == null || heureFin == null || heureDebut[0] < 0 || heureDebut[1] < 0 || heureFin[0] < 0 || heureFin[1] < 0 || heureDebut[0] > 23 || heureDebut[1] > 59 || heureFin[0] > 23 || heureFin[1] > 59 ) {
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être null ou vides");
        }else if (heureDebut[0] > heureFin[0] || (heureDebut[0] == heureFin[0] && heureDebut[1] >= heureFin[1])) {
            throw new IllegalArgumentException("L'heure de début doit être avant l'heure de fin");
        }else{
            this.secouriste = new Secouriste(secouriste.getId(), secouriste.getNom(), secouriste.getPrenom(), secouriste.getDateNaissance(), secouriste.getEmail(), secouriste.getTel(), secouriste.getAdresse(), secouriste.getCompetences(), secouriste.getDisponibilites());
            this.date = new Journee(date.getJour(), date.getMois(), date.getAnnee());
            this.heureDebut = heureDebut.clone();
            this.heureFin = heureFin.clone();
        }
    }

    public Dispos(Secouriste secouriste, LocalDate date, LocalTime heureDebut, LocalTime heureFin) throws IllegalArgumentException{
        if (secouriste == null || date == null || heureDebut == null || heureFin == null) {
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être null ou vides");
        }

        this.secouriste = new Secouriste(secouriste.getId(), secouriste.getNom(), secouriste.getPrenom(), 
                          secouriste.getDateNaissance(), secouriste.getEmail(), secouriste.getTel(), 
                          secouriste.getAdresse(), secouriste.getCompetences(), secouriste.getDisponibilites());
        
        this.date = new Journee(date);
        int hDebut = heureDebut.getHour();
        int mDebut = heureDebut.getMinute();
        int hFin = heureFin.getHour();
        int mFin = heureFin.getMinute();

        this.heureDebut[0] = hDebut;
        this.heureDebut[1] = mDebut;

        this.heureFin[0] = hFin;
        this.heureFin[1] = mFin;
    }

    //================================
    //           GETTERS
    //================================

    /**
     * Returns the secouriste whose disponibility is described by this object
     * @return The secouriste
     */
    public Secouriste getSecouriste() {
        return new Secouriste(
            this.secouriste.getId(), 
            this.secouriste.getNom(), 
            this.secouriste.getPrenom(), 
            this.secouriste.getDateNaissance(), 
            this.secouriste.getEmail(), 
            this.secouriste.getTel(),
            this.secouriste.getAdresse(),
            this.secouriste.getCompetences(),
            this.secouriste.getDisponibilites()
        );
    }

    /**
     * Returns the date of the disponibility
     * @return The date
     */
    public Journee getDate() {
        return new Journee(this.date.getJour(), this.date.getMois(), this.date.getAnnee());
    }

    /**
     * Returns the start time of the disponibility
     * @return The start time
     */
    public int[] getHeureDebut() {
        return heureDebut.clone();
    }

    /**
     * Returns the end time of the disponibility
     * @return The end time
     */
    public int[] getHeureFin() {
        return heureFin.clone();
    }

    //=================================
    //           SETTERS
    // ================================

    /**
     * Sets the disponibility's date
     * @param date The new date
     * @throws IllegalArgumentException if the date is invalid (checked via Journee class)
     */
    public void setDate(Journee date) throws IllegalArgumentException {
        this.date = new Journee(date.getJour(), date.getMois(), date.getAnnee());
    }

    /**
     * Sets the disponibility's start time
     * @param heureDebut The new start time
     * @throws IllegalArgumentException if the start time is invalid
     */
    public void setHeureDebut(int[] heureDeDebut) throws IllegalArgumentException {
        if (heureDeDebut[0] < 0 || heureDeDebut[1] < 0 || heureDeDebut[0] > 23 || heureDeDebut[1] > 59) {
            throw new IllegalArgumentException("L'heure de début doit avoir une heure et des minutes valides");
        }else if (heureDeDebut[0] > this.heureFin[0] || (heureDeDebut[0] == this.heureFin[0] && heureDeDebut[1] >= this.heureFin[1])) {
            throw new IllegalArgumentException("L'heure de début doit être avant l'heure de fin");
        }
        this.heureDebut = heureDebut.clone();
    }

    /**
     * Sets the disponibility's end time
     * @param heureFin The new end time
     * @throws IllegalArgumentException if the end time is invalid
     */
    public void setHeureFin(int[] heureFin) throws IllegalArgumentException {
        if (heureFin[0] < 0 || heureFin[1] < 0 || heureFin[0] > 23 || heureFin[1] > 59) {
            throw new IllegalArgumentException("L'heure de fin doit avoir une heure et des minutes valides");
        }else if (heureFin[0] < this.heureDebut[0] || (heureFin[0] == this.heureDebut[0] && heureFin[1] <= this.heureDebut[1])) {
            throw new IllegalArgumentException("L'heure de fin doit être apres l'heure de debut");
        }
        this.heureFin = heureFin.clone();
    }

    //=================================
    //           METHODS
    // ================================

    @Override
    public boolean equals(Object obj){
        boolean ret = false;

        if (this == obj){
            ret = true;
        } else if (obj instanceof Dispos){
            Dispos autre = (Dispos) obj;
            if(this.getSecouriste().equals(autre.getSecouriste()) && this.getDate().equals(autre.getDate()) && 
            this.getHeureDebut().equals(autre.getHeureDebut()) && this.getHeureFin().equals(autre.getHeureFin())){
                ret = true;
            }
        }
        return ret;
    }

    @Override
    public int hashCode(){
        int ret;
        ret = Objects.hash(getSecouriste(), getDate(), getHeureDebut(), getHeureFin());
        return ret;
    }

    /**
     * Returns a string representation of the disponibility
     * @return The string
     */
    @Override
    public String toString() {
        return date.toString() + " - " + heureDebut[0] + ":" + String.format("%02d", heureDebut[1]) + " - " + heureFin[0] + ":" + String.format("%02d", heureFin[1]);
    }

    public LocalTime toLocalTime(int[] hor){
        return LocalTime.of(hor[0], hor[1]);
    }

    public LocalDateTime debutToLocalDateTime(){
        return LocalDateTime.of(this.date.toLocalDate(), toLocalTime(heureDebut));
    }

    public LocalDateTime finToLocalDateTime(){
        return LocalDateTime.of(this.date.toLocalDate(), toLocalTime(heureFin));
    }
}