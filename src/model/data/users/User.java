package model.data.users;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import model.dao.UserDAO;
import model.data.persistence.Secouriste;
import model.data.service.UserMngt;

/**
 * Classe de base représentant un utilisateur de l'application
 */
public abstract class User {
    private int id;
    private String login;
    private String password;
    private boolean isAdmin;

    private final UserDAO userDAO = new UserDAO();

    public User(int id, String login, String password, boolean isAdmin) {
        this.id = id;
        this.login = login;
        this.password = UserMngt.hashPassword(password);
        this.isAdmin = isAdmin;
    }

    /**
     * Default constructor
     */
    public User(String login, String password, boolean isAdmin) {
        this.login = login;
        this.password = UserMngt.hashPassword(password);
        this.isAdmin = isAdmin;
        int id = userDAO.create(this);
    }

    //================================
    //           GETTERS
    //================================

    /**
     * Returns the user's ID
     * @return the ID of the user
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the user's login
     * @return the login of the user
     */
    public String getLogin() {
        return login;
    }

    /**
     * Returns the user's password
     * @return the password of the user
     * @note The password is stored hashed in the database
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns true if the user is an administrator, false otherwise
     * @return whether the user is an administrator or not
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    //================================
    //           SETTERS
    //================================

    /**
     * Sets the user's ID
     * @param id the new ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the user's login
     * @param login the new login
     * @throws IllegalArgumentException if the login is null or empty
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Sets the user's password
     * @param password the new password to set
     * @note The password should be stored hashed; ensure it is hashed before setting
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets whether the user is an administrator or not
     * @param admin true if the user is an administrator, false otherwise
     */
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

}