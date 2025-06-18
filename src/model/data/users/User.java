package model.data.users;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import model.dao.UserDAO;
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
        this.password = UserMngt.hashPassword(password); // hash password;
        this.isAdmin = isAdmin;
    }

    /**
     * Default constructor
     */
    public User(String login, String password, boolean isAdmin) {
        this.login = login;
        this.password = UserMngt.hashPassword(password); // hash password;
        this.isAdmin = isAdmin;
        int id = userDAO.create(this); // id is set in create method
    }

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