package view;

import java.sql.*;

/**
 * Class to manage the connection to the database
 * 
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */
public class MyConnection {

    /** URL of the database */
    private static final String URL = "jdbc:mysql://10.1.1.1:3306/db_SAE";

    /** Login to connect to the database */
    private static final String LOGIN = "admin";

    /** Password to connect to the database */
    private static final String PWD = "mdp_admin";

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
    public Connection getConnection(){
        Connection connection = null;
        try{
            if (myConnection == null || connection.isClosed()){
                try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException e){
                    e.printStackTrace();
                }
                connection = DriverManager.getConnection(URL, LOGIN, PWD);
            }
        }catch (SQLException f){
            f.printStackTrace();
        }
    
        return connection;
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