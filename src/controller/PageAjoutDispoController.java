package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import model.data.users.UserSecouriste;

public class PageAjoutDispoController {
    public Button BouttonAccueil;
    public Label DateDuJour;
    public MenuButton MenuHeureDebut;
    public MenuButton MenuHeureFin;
    public Button BouttonEnregistrer;
    private UserSecouriste user;

    public void initialize(UserSecouriste user) {
        this.user = user;
    }

    public void calendrierHandle(ActionEvent actionEvent) {
    }
}
