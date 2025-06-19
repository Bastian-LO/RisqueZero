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
        this.idSecouriste = id;
        SecouristeMngt secMngt = new SecouristeMngt();
        this.secouriste = secMngt.findSecouriste(idSecouriste);
    }

    public UserSecouriste(String login, String password) {
        super(login, password, false);
        this.idSecouriste = this.getId();
        SecouristeMngt secMngt = new SecouristeMngt();
        this.secouriste = secMngt.findSecouriste(idSecouriste);
    }

    public int getIdSecouriste() {
        return idSecouriste;
    }

    public void setIdSecouriste(int idSecouriste) {
        this.idSecouriste = idSecouriste;
    }

    public void setSecouriste(Secouriste secouriste) {
        this.secouriste = secouriste;
    }

    public Secouriste getSecouriste() {
        return secouriste;
    }
}