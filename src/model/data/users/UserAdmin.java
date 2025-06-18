package model.data.users;

/**
 * Class representing an admin user
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */
public class UserAdmin extends User {
    public UserAdmin(int id, String login, String password) {
        super(id, login, password, true);
    }
}