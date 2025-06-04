package model;
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
<<<<<<< HEAD
    public void setPassword(String password);

    
=======
    public void setPassword(String password){
        this.password = password;
    }
>>>>>>> 02f676ccee1a520e10def6f581d4527402747aa2
}