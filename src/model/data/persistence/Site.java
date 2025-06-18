package model.data.persistence;

/**
 * Class initializing a place where an event will take place
 */
public class Site {

    //================================
    //           ATTRIBUTES
    //================================

    /**
     * The code of the place
     */
    private String code;

    /**
     * The name of the place
     */
    private String nom;

    /**
     * The longitude coordinate of the place
     */
    private float longitude;

    /**
     * The latitude coordinate of the place
     */
    private float latitude;


    //=================================
    //           CONSTRUCTORS
    // ================================

    /**
     * Constructor
     * @param code The code of the place
     * @param nom The name of the place
     * @param longitude The longitude coordinate of the place
     * @param latitude The latitude coordinate of the place
     * @throws IllegalArgumentException thrown if the parameters are incorrect
     */
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


    //================================
    //           GETTERS
    //================================

    /**
     * Getter for the code
     * @return the code
     */
    public String getCode(){
        return this.code;
    }

    /**
     * Getter for the name
     * @return the name
     */
    public String getNom(){
        return this.nom;
    }

    /**
     * Getter for the longitude
     * @return the longitude
     */
    public float getLongitude(){
        return this.longitude;
    }

    /**
     * Getter for the latitude
     * @return the latitude
     */
    public float getLatitude(){
        return this.latitude;
    }


    //================================
    //           SETTERS
    //================================

    /**
     * Setter for the code
     * @param newCode the new code
     */
    public void setCode(String newCode){
        if (newCode == null || newCode.trim().equals("")){
            throw new IllegalArgumentException("setCode : paramètre invalide");
        }

        this.code = newCode;
    }

    /**
     * Setter for the name
     * @param newNom the new name
     */
    public void setNom(String newNom){
        if (newNom == null || newNom.trim().equals("")){
            throw new IllegalArgumentException("setNom : paramètre invalide");
        }

        this.nom = newNom;
    }

    /**
     * Setter for the longitude
     * @param newLongitude the new longitude
     */
    public void setLongitude(float newLongitude){
        if (newLongitude < -180 || newLongitude > 180){
            throw new IllegalArgumentException("setLongitude : paramètre invalide");
        }

        this.longitude = newLongitude;
    }

    /**
     * Setter for the latitude
     * @param newLatitude the new latitude
     */
    public void setLatitude(float newLatitude){
        if (newLatitude < -90 || newLatitude > 90){
            throw new IllegalArgumentException("setLatitude : paramètre invalide");
        }

        this.latitude = newLatitude;
    }


    //=================================
    //           METHODS
    // ================================

    /**
     * Returns the name of the location
     * @return the name of the location
     */
    @Override
    public String toString(){
        return nom;
    }
}
