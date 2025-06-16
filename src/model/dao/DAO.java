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

    public abstract List <T> findAll ();
    public abstract int update(T element);
    public abstract int delete(T element);
    public abstract int create(T element);
}