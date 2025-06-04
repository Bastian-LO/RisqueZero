package controller;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import view.*;

// Importation des outils java
import java.util.List;
import java.util.LinkedList;
import model.*;
import view.*;


public class Controller {   

    private User user;

    public Controller(User user){
        if (user != null){
            this.user = user;
        } else {
            throw new IllegalArgumentException();
        }
    }
    

    public int create() {
        int ret = 0;
        String query = "INSERT INTO USER(LOGIN , PWD) VALUES ('" + user.getLogin() + "','" + user.getPassword() + "')";
        MyConnection mc = MyConnection.getMyConnection();
        try (Connection con = mc.getConnection();
        Statement st = con.createStatement()) {
            ret = st.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
            ret = -1;
        }
        return ret;
    }

    public List<User> findAll(){
        List<User> users = new LinkedList<>();
        MyConnection mc = MyConnection.getMyConnection();
        try (Connection con = mc.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM USER")) {
            while (rs.next()) {
                String nom = rs.getString("LOGIN");
                String pwd = rs.getString("PWD");
                users.add(new User(nom , pwd));
            }
        } catch (SQLException ex) {
            ex.printStackTrace ();
        }
        return users;
    }

    public User findByLoginPwd2(String login , String pwd) {
        User ret = null;
        MyConnection mc = MyConnection.getMyConnection();
        try (Connection con = mc.getConnection(); PreparedStatement st = con.prepareStatement("SELECT * FROM USER WHERE LOGIN= ? AND PWD= ?")) {
            st.setString(1, login); st.setString(2, pwd);
            
            try(ResultSet rs = st.executeQuery ()){
                if (rs.next()) {
                    String l = rs.getString("LOGIN");
                    String p = rs.getString("PWD");
                    ret = new User(l, p);
                }
            } catch (SQLException ex) { 
                ex.printStackTrace (); 
            }
        } catch (SQLException ex) { 
            ex.printStackTrace (); 
        }
        return ret;
    }

    public int update(User user) {
        int ret = -1;
        String query = "UPDATE USER SET LOGIN='" + user.getLogin () + "', PWD='" + user.getPassword() + "' WHERE LOGIN='"+ user.getLogin () + "'";
        MyConnection mc = MyConnection.getMyConnection();
        try (Connection con = mc.getConnection(); Statement st = con.createStatement ()){
            ret = st.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace ();
            ret = -1;
        }
        return ret;
    }
    
    public int delete(User user) {
        int ret = 0;
        String query = "DELETE FROM USER WHERE LOGIN='" + user.getLogin () + "'";
        MyConnection mc = MyConnection.getMyConnection();
        try (Connection con = mc.getConnection(); Statement st = con.createStatement()) {
            ret = st.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace ();
            ret = -1;
        }
        return ret;
    }


    public static void exportToCSV(String tableName, String outputFilePath) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultats = null;
        FileWriter fileWriter = null;

        try {
            MyConnection mc = MyConnection.getMyConnection();
            connection = mc.getConnection();
            
            // Créer et exécuter la requête
            statement = connection.createStatement();
            resultats = statement.executeQuery("SELECT * FROM " + tableName);
            
            // Préparer le fichier de sortie
            fileWriter = new FileWriter(outputFilePath);
            
            // 4. Écrire l'en-tête CSV (noms des colonnes)
            ResultSetMetaData metaData = resultats.getMetaData();
            int nbColonnes = metaData.getColumnCount();
            
            for (int i = 1; i <= nbColonnes; i++) {
                fileWriter.append(metaData.getColumnName(i));
                if (i < nbColonnes) {
                    fileWriter.append(",");
                }
            }
            fileWriter.append("\n");
            
            // 5. Écrire les données
            while (resultats.next()) {
                for (int i = 1; i <= nbColonnes; i++) {
                    String value = resultats.getString(i);
                    // Gérer les valeurs contenant des virgules (entourer de guillemets)
                    if (value != null && value.contains(",")) {
                        value = "\"" + value + "\"";
                    }
                    fileWriter.append(value != null ? value : "");
                    
                    if (i < nbColonnes) {
                        fileWriter.append(",");
                    }
                }
                fileWriter.append("\n");
            }
            
            System.out.println("Export CSV réussi : " + outputFilePath);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            // 6. Fermer les ressources
            try {
                if (resultats != null) resultats.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
                if (fileWriter != null) fileWriter.close();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}