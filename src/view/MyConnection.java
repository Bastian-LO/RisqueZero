package view;

import java.sql.*;

/**
 * Class to manage the connection to the database
 * 
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */
public class MyConnection {

    /** URL of the database */
    private static final String URL = "jdbc:mysql://localhost:3306/db_SAE";

    /** Login to connect to the database */
    private static final String LOGIN = "adminClient";

    /** Password to connect to the database */
    private static final String PWD = "mdp_adminClient";

    /** Connection to the database */
    private Connection conn = null;

    /** Unique instance of the class */
    private static MyConnection myConnection = null;

    /** 
     * Constructor which does nothing
     * to prevent several instances
     * of the class (Singleton pattern)
     */
    private MyConnection(){} 

    /**
     * Method to get the connection to the database
     */
    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace ();
        }
        
        if (conn == null || conn.isClosed()) {
            try {
                conn = DriverManager.getConnection(URL, LOGIN, PWD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return conn;
    }

    /**
     * Method to get the unique instance of the class
     */
    public static MyConnection getMyConnection(){
        if(myConnection == null){
            myConnection = new MyConnection();
        }
        return myConnection;
    }
}