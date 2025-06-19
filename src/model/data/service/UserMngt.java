package model.data.service;

import model.data.users.User;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import model.dao.UserDAO;

public class UserMngt{

    private final UserDAO userDAO = new UserDAO();

    /**
     * Verify the given password for the given login
     * @param login the login to verify
     * @param password the password to verify
     * @return true if the password is valid, false otherwise
     */
    public boolean verifyPassword(String login, String password) {
        User user = userDAO.findByLogin(login);
        if (user == null){
            return false;
        }
        String hashedInput = hashPassword(password);
        return hashedInput.equals(user.getPassword());
    }

    /**
     * Hashes a password using SHA-256 algorithm.
     * Converts the password into a hexadecimal string representation of the hash.
     *
     * @param password the plain text password to be hashed
     * @return the hashed password as a hexadecimal string
     * @throws RuntimeException if the SHA-256 algorithm is not available
     */
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

    /**
     * Finds a user by its login
     * @param login the login of the user to search
     * @return the user object if found, null otherwise
     */
    public User findUser(String login) {
        return userDAO.findByLogin(login);
    }

}