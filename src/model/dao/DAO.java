package model.dao;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import view.MyConnection;

/**
 * Abstract class DAO for all DAO
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */
public abstract class DAO <T> {
    
    protected Connection getConnection() throws SQLException {
        return MyConnection.getMyConnection().getConnection();
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

    public abstract List <T> findAll ();
    public abstract int update(T element);
    public abstract int delete(T element);
    public abstract int create(T element);
}