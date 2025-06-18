package model.data.persistence;

/**
 * Class representing a sport
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */
public class Sport {

    //====================================
    //            ATTRIBUTES
    //====================================

    /** Code of the sport */
    private String code;

    /** Name of the sport */
    private String nom;


    //====================================
    //            CONSTRUCTORS
    //====================================

    /**
     * Constructor initializing a sport
     * @param code the code identifying the sport
     * @param nom the name of the sport
     * @throws IllegalArgumentException if the parameters are invalid (null or empty)
     */
    public Sport(String code, String nom) throws IllegalArgumentException {
        // Check if the parameters are valid
        if (code == null || code.trim().equals("") || nom == null || nom.trim().equals("")){
            throw new IllegalArgumentException("Paramètres invalides");
        }

        this.code = code;
        this.nom = nom;
    }


    //====================================
    //              GETTERS
    //====================================

    /**
     * Getter for the code
     * @return the code of the sport
     */
    public String getCode(){
        return this.code;
    }

    /**
     * Getter for the name
     * @return the name of the sport
     */
    public String getNom(){
        return this.nom;
    }


    //====================================
    //              SETTERS
    //====================================

    /**
     * Setter for the code
     * @param newCode the new code
     * @throws IllegalArgumentException if the new code is invalid (null or empty)
     */
    public void setCode(String newCode) throws IllegalArgumentException {
        // Check if the new code is valid
        if (newCode == null || newCode.trim().equals("")){
            throw new IllegalArgumentException("Paramètre invalide");
        }
        this.code = newCode;
    }

    /**
     * Setter for the name
     * @param newNom the new name
     * @throws IllegalArgumentException if the new name is invalid (null or empty)
     */
    public void setNom(String newNom) throws IllegalArgumentException {
        // Checks if the new name is valid
        if(newNom == null || newNom.trim().equals("")){
            throw new IllegalArgumentException("Paramètre invalide");
        }
        this.nom = newNom;
    }


    //=================================
    //           METHODS
    // ================================

    /**
     * Returns the sport's name
     * @return the sport's name
     */
    @Override
    public String toString(){
        return nom;
    }
}