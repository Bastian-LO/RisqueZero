package model.data.persistence;

public class Sport {
    private String code;
    private String nom;

    public Sport(String code, String nom){
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

    public void setCode(String newCode){
        if (newCode == null || newCode.trim().equals("")){
            throw new IllegalArgumentException("Paramètre invalide");
        }
        this.code = newCode;
    }

    public void setNom(String newNom){
        if(newNom == null || newNom.trim().equals("")){
            throw new IllegalArgumentException("Paramètre invalide");
        }
        this.nom = newNom;
    }
}