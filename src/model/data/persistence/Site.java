package model.data.persistence;

public class Site {
    private String code;
    private String nom;
    private float longitude;
    private float latitude;

    public Site(String code, String nom, float longitude, float latitude) throws IllegalArgumentException{
        if(code == null || code.trim().equals("") || nom == null || nom.trim().equals("") || 
        longitude < -180 || longitude > 180 || latitude < -90 || latitude > 90){
            throw new IllegalArgumentException("Site : paramètre(s) invalide(s)");
        }

        this.code = code;
        this.nom = nom;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getCode(){
        return this.code;
    }

    public String getNom(){
        return this.nom;
    }

    public float getLongitude(){
        return this.longitude;
    }

    public float getLatitude(){
        return this.latitude;
    }

    public void setCode(String newCode){
        if (newCode == null || newCode.trim().equals("")){
            throw new IllegalArgumentException("setCode : paramètre invalide");
        }

        this.code = newCode;
    }

    public void setNom(String newNom){
        if (newNom == null || newNom.trim().equals("")){
            throw new IllegalArgumentException("setNom : paramètre invalide");
        }

        this.nom = newNom;
    }
}
