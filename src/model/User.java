package model;
/**
 * Interfaces which represents a user of the application
 * 
 * @au
 */
public interface User {

    //================================
    //           GETTERS
    //================================

    /**
     * Returns the login of the user
     */
    public String getLogin();

    /**
     * Returns the password of the user
     */
    public String getPassword();

    //================================
    //           SETTERS
    //================================

    /**
     * Sets the login of the user
     */
    public void setLogin(String login);

    /**
     * Sets the password of the user
     */
    public void setPassword(String password);
}