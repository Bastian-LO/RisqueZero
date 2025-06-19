package view;

import java.sql.*;

/**
 * Class to manage the connection to the database
 * 
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */
public class MyConnection {

    /** URL of the database */
    private static final String URL = "jdbc:mariadb://10.1.1.1:3306/db_SAE?autoReconnect=true&connectTimeout=3000";

    /** Login to connect to the database */
    private static final String LOGIN = "adminClient";

    /** Password to connect to the database */
    private static final String PWD = "mdp_adminClient";

    /**
     * Returns a connection to the database
     * @return a connection to the database
     * @throws SQLException if there is an issue with the database
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, LOGIN, PWD);
    }
}