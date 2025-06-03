package model.data.persistence;

public class Secouriste {
    private long id;
    private String nom;
    private String prenom;
    private String dateNaissance;
    private String email;
    private String tel;
    private String adresse;

    public Secouriste(long id, String nom, String prenom, String dateNaissance, String email, String tel, String adresse) throws IllegalArgumentException{
        if(id == null || nom == null || nom.equals("") || nom == null || nom.equals("") || prenom == null || prenom.equals("") || dateNaissance == null || dateNaissance ){
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être null ou vides");
        }else if(dateNaissance.equals("" || )){
            
        }
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
