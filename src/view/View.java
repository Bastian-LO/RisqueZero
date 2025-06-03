package view;

import java.sql.*;

public class View {
    public static void main(String[] args){
        MyConnection mc = MyConnection.getMyConnection();
        Connection c = mc.getConnection();
    }
}