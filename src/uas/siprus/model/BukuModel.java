/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uas.siprus.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author null
 */
public class BukuModel {
    static BukuModel instance;
    
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "perpustakaan";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";
    
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://"+DB_HOST+":"+DB_PORT+"/"+DB_NAME;
    
    private Connection conn;
    private PreparedStatement statement;
    
    public static BukuModel getIntstance() throws SQLException{
        if (instance == null) {
            instance = new BukuModel();
        }
        return instance;
    }
    
    public BukuModel() throws SQLException{
        bukaKonDB();
    }
    
    private Connection bukaKonDB() throws SQLException{
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            if (conn != null) {
                System.out.println("Successfully connected to datebase.");
            }
            return conn;
        }catch(ClassNotFoundException ex){
            ex.printStackTrace();
        }
        return null;
    }
    
    public void tutupKonDB(){
        try{
            if (statement !=null && !statement.isClosed()) {
                statement.close();
                System.out.println("Statement closed");
            }
            if (conn !=null && !conn.isClosed()) {
                conn.close();
                System.out.println("Connection closed");
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}
