package model.data.service;

import model.data.users.User;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import model.dao.UserDAO;
public class UserMngt{

    private final UserDAO userDAO = new UserDAO();

    public boolean verifyPassword(String login, String password) {
        User user = userDAO.findByLogin(login);
        if (user == null){
            return false;
        }
        String hashedInput = hashPassword(password);
        return hashedInput.equals(user.getPassword());
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}