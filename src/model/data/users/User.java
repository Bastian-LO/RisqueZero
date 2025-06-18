package model.data.users;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe de base représentant un utilisateur de l'application
 */
public abstract class User {
    private int id;
    private String login;
    private String password;
    private boolean isAdmin;

    public User(int id, String login, String password, boolean isAdmin) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    /**
     * Default constructor
     */
    public User() {}

    //================================
    //           GETTERS
    //================================

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    //================================
    //           SETTERS
    //================================

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

}