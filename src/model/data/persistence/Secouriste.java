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
        if (id < 0) {
            throw new IllegalArgumentException("L'ID doit être supérieur ou égal à 0 (valeur reçue: " + id + ")");
        }

        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne doit pas être null ou vide");
        }

        if (prenom == null || prenom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne doit pas être null ou vide");
        }

        if (dateNaissance == null) {
            throw new IllegalArgumentException("La date de naissance ne doit pas être null");
        }

        if (!dateNaissance.trim().matches("^\\d{2}/\\d{2}/\\d{4}$")) {
            throw new IllegalArgumentException("La date de naissance doit être au format JJ/MM/AAAA (valeur reçue: '" + dateNaissance + "')");
        }

        if (email == null) {
            throw new IllegalArgumentException("L'email ne doit pas être null");
        }

        if (!email.trim().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("L'email doit être au format exemple@domaine.com (valeur reçue: '" + email + "')");
        }

        if (tel == null) {
            throw new IllegalArgumentException("Le numéro de téléphone ne doit pas être null");
        }

        if (!tel.trim().matches("^0\\d{9}$")) {
            throw new IllegalArgumentException("Le téléphone doit être au format 0123456789 (10 chiffres commençant par 0, valeur reçue: '" + tel + "')");
        }

        if (adresse == null || adresse.trim().isEmpty()) {
            throw new IllegalArgumentException("L'adresse ne doit pas être null ou vide");
        }
        if(comp == null || comp.contains(null)){
            throw new IllegalArgumentException("La liste de Competence ne peut pas être null");
        }
        if(disponibilite == null){
            throw new IllegalArgumentException("L'ensemble de Dispos ne peut pas être null");
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
     * Returns the full name
     * @return the full name
     */
    public String getNomComplet() {
        return nom + " " + prenom;
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
     * Method adding a disponibility to a secourist without automatic merging
     * @param newDispo the new disponibility to add
     * @throws IllegalArgumentException if the parameter is invalid
     */
    public void addDispos(Dispos newDispo) {
        // Pré-condition
        if (newDispo == null || !newDispo.getSecouriste().equals(this)) {
            throw new IllegalArgumentException("addDispos : paramètre invalide");
        }
        
        // Crée une nouvelle copie pour éviter les modifications inattendues
        HashSet<Dispos> updatedDispos = new HashSet<>(this.disponibilites);
        
        // Vérifie d'abord s'il existe une disponibilité identique
        boolean exists = updatedDispos.stream().anyMatch(d -> 
            d.getDate().equals(newDispo.getDate()) &&
            Arrays.equals(d.getHeureDebut(), newDispo.getHeureDebut()) &&
            Arrays.equals(d.getHeureFin(), newDispo.getHeureFin())
        );
        
        if (!exists) {
            updatedDispos.add(newDispo);
            this.disponibilites = updatedDispos;
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