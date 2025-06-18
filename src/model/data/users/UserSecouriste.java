package model.data.users;

/**
 * Class representing a secouriste user
 * 
 */
public class UserSecouriste extends User {
    private int idSecouriste;

    public UserSecouriste(int id, String login, String password, int idSecouriste) {
        super(id, login, password, false);
        this.idSecouriste = idSecouriste;
    }

    public UserSecouriste(String login, String password) {
        super(login, password, false);
    }

    public int getIdSecouriste() {
        return idSecouriste;
    }

    public void setIdSecouriste(int idSecouriste) {
        this.idSecouriste = idSecouriste;
    }
}