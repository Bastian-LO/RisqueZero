package model.dao;

import model.data.users.User;
import model.data.users.UserAdmin;
import model.data.users.UserSecouriste;
import model.data.service.UserMngt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for User, and its subclasses
 */
public class UserDAO extends DAO<User> {

    /**
     * Get all users from database
     * @return List of users
     */
    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.id, u.login, u.mot_de_passe, u.est_admin, s.id AS id_secouriste "
                   + "FROM utilisateur u "
                   + "LEFT JOIN secouriste s ON u.id = s.id_utilisateur";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(createUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Get user by id
     * @param id id of the user
     * @return user the user with the given id or null if not found
     */
    public User findById(int id) {
        String sql = "SELECT * FROM utilisateur "
                   + "WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createUserFromResultSet(rs, id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get user by login
     * @param login login of the user
     * @return user the user with the given login or null if not found
     */
    public User findByLogin(String login) {
        String sql = "SELECT * "
                   + "FROM utilisateur "
                   + "WHERE login = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, login);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    return createUserFromResultSet(rs, id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a new user in the database. The generated id is retrieved from the database
     * and set to the user object. If an error occurs, -1 is returned.
     *
     * @param user the user object to be created
     * @return the generated id of the user, or -1 if an error occurs
     */
    @Override
    public int create(User user) {
            String sql = "INSERT INTO utilisateur (login, mot_de_passe, est_admin) VALUES (?, ?, ?)";
            try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, user.getLogin());
                stmt.setString(2, UserMngt.hashPassword(user.getPassword()));
                stmt.setBoolean(3, user.isAdmin());
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        user.setId(generatedId);
                        return 1;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Une erreur est survenue lors de la création de l'utilisateur.");
            }
            return -1;
    }

    @Override
    public int update(User user) {
        //Pour se protéger des injections SQL, pas de mise à jour des users
        throw new UnsupportedOperationException("Il est interdit de mettre à jour un utilisateur.");
    }

    @Override
    public int delete(User user) {
        String sql = "DELETE FROM utilisateur WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, user.getId());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Creates a User object based on a ResultSet row.
     * The id attribute of the User object is set to the value of the "id" column of the ResultSet.
     * We do this because the id is auto-incremented in the database.
     * The method creates a User in the database, and then calls the 2nd
     * method to create the User object with the retrieved id.
     * @param rs the ResultSet containing the data to create the User from.
     * @return the created User object.
     * @throws SQLException if there is an issue accessing the ResultSet.
     */
    public User createUserFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        return createUserFromResultSet(rs, id);
    }


    /**
     * Creates a User object based on a ResultSet row, given the id of the user.
     * The method creates a User with the given id, and then sets the attributes of the User object using the data from the ResultSet.
     * @param rs the ResultSet containing the data to create the User from.
     * @param id the id of the user to create.
     * @return the created User object.
     * @throws SQLException if there is an issue accessing the ResultSet.
     */
    public User createUserFromResultSet(ResultSet rs, int id) throws SQLException {
        String login = rs.getString("login");
        String password = rs.getString("mot_de_passe");
        boolean isAdmin = rs.getBoolean("est_admin");
        
        if (isAdmin) {
            return new UserAdmin(id, login, password);
        } else {
            return new UserSecouriste(id, login, password);
        }
    }
}