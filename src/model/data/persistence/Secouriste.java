package model.data.persistence;
import java.time.*;
import java.util.*;

/**
 * Class defining a secourist with its personal information, its disponibility and its skills
 * @author Killian Avril, Bastian Le Ouedec, Enrick Mananjean, Emile Thevenin, Elwan Yvin
 */
public class Secouriste{

    //================================
    //           ATTRIBUTES
    //================================
    
    /**
     * The ID of the secourist
     */
    private long id;

    /**
     * The name of the secourist
     */
    private String nom;

    /**
     * The first name of the secourist
     */
    private String prenom;

    /**
     * The birth date of the secourist
     */
    private String dateNaissance;

    /**
     * The email of the secourist
     */
    private String email;

    /**
     * The phone number of the secourist
     */
    private String tel;

    /**
     * The address of the secourist
     */
    private String adresse;

    /**
     * The secourist's disponibilities
     */
    private HashSet<Dispos> disponibilites;

    /**
     * The secourist's competences
     */
    private ArrayList<Competence> competences = new ArrayList<>();


    //=================================
    //           CONSTRUCTORS
    // ================================

    /**
     * Constructor
     * @param id the ID
     * @param nom the name
     * @param prenom the first name
     * @param dateNaissance the birth date
     * @param email the email
     * @param tel the phone number
     * @param adresse the address
     * @param comp the list of competence
     * @param disponibilite the set of disponibilities
     * @throws IllegalArgumentException if a parameter is invalid
     */
    public Secouriste(long id, String nom, String prenom, String dateNaissance, String email, String tel, String adresse, ArrayList<Competence> comp, HashSet<Dispos> disponibilite) throws IllegalArgumentException{
        // Checks if the parameters are valid
        if(id < 1 || nom == null || nom.trim().equals("") || prenom == null || prenom.trim().isEmpty() || dateNaissance == null || !dateNaissance.trim().matches("^\\d{2}/\\d{2}/\\d{4}$") ||
        email == null || !email.trim().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$") || tel == null || 
        !tel.trim().matches("^0\\d{9}$") || adresse == null || adresse.trim().isEmpty()){
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être null ou vides");
        }
        if(comp == null || comp.isEmpty() || comp.contains(null)){
            throw new IllegalArgumentException("La liste de Competence ne peut pas être null ou vides");
        }
        if(disponibilite == null){
            throw new IllegalArgumentException("L'ensemble de Dispos ne peut pas être null ou vides");
        }

        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.email = email;
        this.tel = tel;
        this.adresse = adresse;
        this.disponibilites = disponibilite;
        this.competences = comp;
    }
    

    //================================
    //           GETTERS
    //================================

    /**
     * Returns the ID
     * @return the ID
     */
    public long getId(){
        return id;
    }
    
    /**
     * Returns the last name
     * @return the last name
     */
    public String getNom() {
        return nom;
    }

    /**
     * Returns the first name
     * @return the first name
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Returns the birth date
     * @return the birth date
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * Returns the email
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the phone number
     * @return the phone number
     */
    public String getTel() {
        return tel;
    }

    /**
     * Returns the address
     * @return the address
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * Returns the list of competences
     * @return the list of competences
     */
    public ArrayList<Competence> getCompetences(){
        return new ArrayList<>(this.competences);
    }
    
    /**
     * Returns the set of disponibilities
     * @return the set of disponibilities
     */
    public HashSet<Dispos> getDisponibilites(){
        return new HashSet<Dispos>(this.disponibilites);
    }

    /**
     * Returns the list of competences's names
     * @return the list of names
     */
    public ArrayList<String> getCompetencesIntitules(){
        ArrayList<String> ret = new ArrayList<>();
        for (int i = 0; i < this.getCompetences().size(); i++){
            Competence compCurr = this.getCompetences().get(i);
            ret.add(compCurr.getIntitule());
        }
        return ret;
    }


    //================================
    //           SETTERS
    //================================

    /**
     * Setter for the id
     * @param id the new ID
     */
    public void setId(long id){
        this.id = id;
    }

    /**
     * Setter for the name
     * @param nom the new name
     * @throws IllegalArgumentException if the name is invalid
     */
    public void setNom(String nom) throws IllegalArgumentException{
        if(nom == null || nom.trim().equals("")){
            throw new IllegalArgumentException();
        }
        this.nom = nom;
    }

    /**
     * Setter for the first name
     * @param prenom the new first name
     */
    public void setPrenom(String prenom) {
        if(prenom == null || prenom.trim().equals("")){
            throw new IllegalArgumentException();
        }
        this.prenom = prenom;
    }

    /**
     * Setter for the birth date
     * @param dateNaissance the new birth date
     */
    public void setDateNaissance(String dateNaissance) {
        if(dateNaissance == null || !dateNaissance.trim().matches("^\\d{2}/\\d{2}/\\d{4}$")){
            throw new IllegalArgumentException();
        }
        this.dateNaissance = dateNaissance;
    }

    /**
     * Setter for the email
     * @param email the new email
     */
    public void setEmail(String email) {
        if(email == null || !email.trim().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")){
            throw new IllegalArgumentException();
        }
        this.email = email;
    }

    /**
     * Setter for the phone number
     * @param tel the new phone number
     */
    public void setTel(String tel) {
        if(tel == null || !tel.trim().matches("^0\\d{9}$")){
            throw new IllegalArgumentException();
        }
        this.tel = tel;
    }

    /**
     * Setter for the address
     * @param adresse the new address
     */
    public void setAdresse(String adresse) {
        if(adresse == null || adresse.trim().equals("")){
            throw new IllegalArgumentException();
        }
        this.adresse = adresse;
    }

    /**
     * Setter for the set of disponibility
     * @param newDispos
     */
    public void setDisponibilites(HashSet<Dispos> newDispos){
        if (newDispos == null || newDispos.isEmpty() || newDispos.contains(null)){
            throw new IllegalArgumentException("setDisponibilites : paramètre invalide");
        }

        this.disponibilites.clear();
        for(Dispos dispo : newDispos){
            this.disponibilites.add(dispo);
        }
    }


    //=================================
    //           METHODS
    // ================================

    /**
     * Method adding a disponibility to a secourist and merging them if they overlap
     * @param newDispo the new disponibility
     */
    public void addDispos(Dispos newDispo) {
        // Pré-condition
        if (newDispo == null || !newDispo.getSecouriste().equals(this)) {
            throw new IllegalArgumentException("addDispos : paramètre invalide");
        }
        
        HashSet<Dispos> newDispos = this.getDisponibilites();
        
        if(newDispos.isEmpty()){   // Si la liste de dispos est vide, on ajoute directement
            newDispos.add(newDispo);
            this.setDisponibilites(newDispos);
        } else {
            LocalTime newStart = newDispo.toLocalTime(newDispo.getHeureDebut());
            LocalTime newEnd = newDispo.toLocalTime(newDispo.getHeureFin());
            LocalDate date = newDispo.getDate().toLocalDate();

            Dispos mergedDispo = new Dispos(this, date, newStart, newEnd);
            ArrayList<Dispos> toRemove = new ArrayList<Dispos>();
            boolean identicalExists = false;

            HashSet<Dispos> disponibilites = this.getDisponibilites();

            for (Dispos dispo : disponibilites) {
                LocalDate dispoDate = dispo.getDate().toLocalDate();
                if (dispoDate.equals(date)) {
                    LocalTime dispoStart = dispo.toLocalTime(dispo.getHeureDebut());
                    LocalTime dispoEnd = dispo.toLocalTime(dispo.getHeureFin());

                    boolean overlaps = !(newEnd.isBefore(dispoStart) || newStart.isAfter(dispoEnd));
                    if (overlaps) {
                        LocalTime mergedStart = newStart.isBefore(dispoStart) ? newStart : dispoStart;
                        LocalTime mergedEnd = newEnd.isAfter(dispoEnd) ? newEnd : dispoEnd;
                        mergedDispo = new Dispos(this, date, mergedStart, mergedEnd);
                        toRemove.add(dispo);
                    }

                    boolean isIdentical = newStart.equals(dispoStart) && newEnd.equals(dispoEnd);
                    if (isIdentical) {
                        identicalExists = true;
                    }
                }
            }

            for (Dispos d : toRemove) {
                disponibilites.remove(d);
            }

            if (!identicalExists) {
                disponibilites.add(mergedDispo);
            }
        }
    }

    /**
     * Checks if the current secourist is equal to the object in parameter
     * @return true if they are the same secourist
     */
    @Override
    public boolean equals(Object obj){
        boolean ret = false;

        if (this == obj){
            ret = true;
        } else if (obj instanceof Secouriste){
            Secouriste autre = (Secouriste) obj;
            if(this.getId() == autre.getId()){
                ret = true;
            }
        }
        return ret;
    }

    /**
     * Returns the hashcode of the secourist
     * @return the hashcode
     */
    @Override
    public int hashCode(){
        int ret;
        ret = Objects.hash(getId(), getNom(), getPrenom(), getDateNaissance(), getAdresse(), getEmail(), getTel(), getDisponibilites(), getCompetences());
        return ret;
    }
}