package model.data.users;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Interfaces which represents a user of the application
 * 
 * @author Emile Thevenin
 */
public abstract class User {
    private String login;
    private String password;

    //================================
    //           GETTERS
    //================================

    /**
     * Returns the login of the user
     */
    public String getLogin(){
        return login;
    }

    /**
     * Returns the password of the user
     */
    public String getPassword(){
        return password;
    }

    //================================
    //           SETTERS
    //================================

    /**
     * Sets the login of the user
     */
    public void setLogin(String login){
        this.login = login;
    }

    /**
     * Sets the password of the user
     */
    public void setPassword(String password){
        this.password = password;
    }

    //================================
    //           METHODS
    //================================

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            // Convertir en hexadécimal :
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}   