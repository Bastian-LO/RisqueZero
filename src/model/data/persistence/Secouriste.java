package model.data.persistence;

import model.User;

public class Secouriste extends User{
    private long id;
    private String nom;
    private String prenom;
    private String dateNaissance;
    private String email;
    private String tel;
    private String adresse;

    public Secouriste(long id, String nom, String prenom, String dateNaissance, String email, String tel, String adresse) throws IllegalArgumentException{
        // Checks 
        if(id < 1 || nom == null || nom.equals("") || nom == null || nom.equals("") || prenom == null || 
        prenom.equals("") || dateNaissance == null || !dateNaissance.matches("^\\d{2}/\\d{2}/\\d{4}$") ||
        email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$") || tel == null || 
        !tel.matches("^0\\d{9}$") || adresse == null || adresse.equals("")){
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être null ou vides");
        }

        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.email = email;
        this.tel = tel;
        this.adresse = adresse;
    }
    
    //================================
    //           GETTERS
    //================================
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

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
}
