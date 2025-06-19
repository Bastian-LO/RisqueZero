package model.dao;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import java.sql.ResultSetMetaData;
import java.sql.Statement;

import view.MyConnection;

/**
 * Abstract class DAO for all DAO
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */
public abstract class DAO <T> {
    
    protected Connection getConnection() throws SQLException {
        Connection conn = MyConnection.getConnection();
        if (conn.isClosed()) {
            throw new SQLException("La connexion est fermée");
        }
        return conn; 
    }

    /**
     * Parses a time string into an array of two integers
     * @param time String of the form "HH:MM"
     * @return An array of two integers: {hour, minute}
     */
    protected int[] parseTime(String time) {
        String[] parts = time.split(":");
        return new int[]{
            Integer.parseInt(parts[0]),
            Integer.parseInt(parts[1])
        };
    }

    /**
     * Formats an array of two integers into a string of the form "HH:MM"
     * @param time An array of two integers: {hour, minute}
     * @return A string of the form "HH:MM"
     */
    protected String formatTime(int[] time) {
        return String.format("%02d:%02d", time[0], time[1]);
    }

    /**
     * Export the content of a table in the database to a CSV file
     * @param tableName the name of the table to export
     * @param outputFilePath the path of the output file
     * @throws SQLException if there is an issue with the database
     * @throws IOException if there is an issue with the file
     */
    public static void exportToCSV(String tableName, String outputFilePath) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultats = null;
        FileWriter fileWriter = null;

        try {
            connection = MyConnection.getConnection();
            
            statement = connection.createStatement();
            resultats = statement.executeQuery("SELECT * FROM " + tableName);
            
            fileWriter = new FileWriter(outputFilePath);
            
            ResultSetMetaData metaData = resultats.getMetaData();
            int nbColonnes = metaData.getColumnCount();
            
            for (int i = 1; i <= nbColonnes; i++) {
                fileWriter.append(metaData.getColumnName(i));
                if (i < nbColonnes) {
                    fileWriter.append(",");
                }
            }
            fileWriter.append("\n");
            
            while (resultats.next()) {
                for (int i = 1; i <= nbColonnes; i++) {
                    String value = resultats.getString(i);
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

    public abstract List <T> findAll ();
    public abstract int update(T element);
    public abstract int delete(T element);
    public abstract int create(T element);
}