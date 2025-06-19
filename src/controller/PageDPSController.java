package controller;

import model.data.users.User;
import model.data.users.UserAdmin;

public class PageDPSController {
    public User user;

    public void initialize(UserAdmin user) {
        this.user = user;
    }
}
