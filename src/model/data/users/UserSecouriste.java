package model.data.users;

/**
 * Class representing a secouriste user
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */
public class UserSecouriste extends User {
    private int idSecouriste;

    public UserSecouriste(int id, String login, String password, int idSecouriste) {
        super(id, login, password, false);
        this.idSecouriste = idSecouriste;
    }

    public int getIdSecouriste() {
        return idSecouriste;
    }

    public void setIdSecouriste(int idSecouriste) {
        this.idSecouriste = idSecouriste;
    }
}