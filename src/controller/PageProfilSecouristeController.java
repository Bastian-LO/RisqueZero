package controller;

import model.data.persistence.Secouriste;
import model.data.users.UserSecouriste;

public class PageProfilSecouristeController {
    private UserSecouriste user;
    private Secouriste secouriste;

    // Méthode pour initialiser les données de l'utilisateur
    public void setUser(UserSecouriste user) {
        this.user = user;
        this.secouriste = user.getSecouriste();
    }
}
