package model.data.persistence;

public class Sport {
    private String code;
    private String nom;

    public Sport(String code, String nom) throws IllegalArgumentException {
        // Check if the parameters are valid
        if (code == null || code.trim().equals("") || nom == null || nom.trim().equals("")){
            throw new IllegalArgumentException("Paramètres invalides");
        }

        this.code = code;
        this.nom = nom;
    }

    public String getCode(){
        return this.code;
    }

    public String getNom(){
        return this.nom;
    }

    public void setCode(String newCode) throws IllegalArgumentException {
        // Check if the new code is valid
        if (newCode == null || newCode.trim().equals("")){
            throw new IllegalArgumentException("Paramètre invalide");
        }
        this.code = newCode;
    }

    public void setNom(String newNom) throws IllegalArgumentException {
        // Checks if the new name is valid
        if(newNom == null || newNom.trim().equals("")){
            throw new IllegalArgumentException("Paramètre invalide");
        }
        this.nom = newNom;
    }
}