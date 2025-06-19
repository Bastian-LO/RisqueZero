package model.data.users;

import model.data.persistence.Secouriste;
import model.data.service.SecouristeMngt;

/**
 * Class representing a secouriste user
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */
public class UserSecouriste extends User {
    private int idSecouriste;
    private Secouriste secouriste;


    public UserSecouriste(int id, String login, String password) {
        super(id, login, password, false);
    }

    public UserSecouriste(String login, String password) {
        super(login, password, false);
    }

    /**
     * Returns the ID of the secouriste associated to this user
     * @return the ID of the secouriste
     */
    public int getIdSecouriste() {
        return idSecouriste;
    }

    /**
     * Sets the ID of the secouriste associated to this user
     * @param idSecouriste the ID of the secouriste
     */
    public void setIdSecouriste(int idSecouriste) {
        this.idSecouriste = idSecouriste;
    }

    /**
     * Sets the secouriste associated to this user
     * @param secouriste the secouriste to associate
     */
    public void setSecouriste(Secouriste secouriste) {
        this.secouriste = secouriste;
    }

    /**
     * Retrieves the secouriste associated with this user.
     * 
     * @return the associated secouriste
     */
    public Secouriste getSecouriste() {
        return secouriste;
    }
}