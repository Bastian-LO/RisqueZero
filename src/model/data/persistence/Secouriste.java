package model.data.persistence;
import java.util.HashSet;

public class Secouriste{
    private long id;
    private String nom;
    private String prenom;
    private String dateNaissance;
    private String email;
    private String tel;
    private String adresse;
    private HashSet<Dispos> disponibilité;

    public Secouriste(long id, String nom, String prenom, String dateNaissance, String email, String tel, String adresse) throws IllegalArgumentException{
        // Checks if the parameters are valid
        if(id < 1 || nom == null || nom.trim().equals("") || nom == null || nom.trim().equals("") || 
        prenom == null || prenom.trim().equals("") || dateNaissance == null || !dateNaissance.trim().matches("^\\d{2}/\\d{2}/\\d{4}$") ||
        email == null || !email.trim().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$") || tel == null || 
        !tel.trim().matches("^0\\d{9}$") || adresse == null || adresse.trim().equals("")){
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être null ou vides");
        }

        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.email = email;
        this.tel = tel;
        this.adresse = adresse;
        this.journées = new HashSet<>();
    }
    
    //================================
    //           GETTERS
    //================================
    public long getId(){
        return id;
    }
    
    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getEmail() {
        return email;
    }

    public String getTel() {
        return tel;
    }

    public String getAdresse() {
        return adresse;
    }
    
    //================================
    //           SETTERS
    //================================
    public void setId(long id){
        this.id = id;
    }

    public void setNom(String nom) throws IllegalArgumentException{
        if(nom == null || nom.trim().equals("")){
            throw new IllegalArgumentException();
        }
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        if(prenom == null || prenom.trim().equals("")){
            throw new IllegalArgumentException();
        }
        this.prenom = prenom;
    }

    public void setDateNaissance(String dateNaissance) {
        if(dateNaissance == null || !dateNaissance.trim().matches("^\\d{2}/\\d{2}/\\d{4}$")){
            throw new IllegalArgumentException();
        }
        this.dateNaissance = dateNaissance;
    }

    public void setEmail(String email) {
        if(email == null || !email.trim().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")){
            throw new IllegalArgumentException();
        }
        this.email = email;
    }

    public void setTel(String tel) {
        if(tel == null || !tel.trim().matches("^0\\d{9}$")){
            throw new IllegalArgumentException();
        }
        this.tel = tel;
    }

    public void setAdresse(String adresse) {
        if(adresse == null || adresse.trim().equals("")){
            throw new IllegalArgumentException();
        }
        this.adresse = adresse;
    }
}
