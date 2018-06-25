/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uas.siprus.model;

import com.mysql.jdbc.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//import uas.siprus.Buku;
import uas.siprus.Buku;

/**
 *
 * @author null
 */
public class BukuModel extends AllModel{

    static BukuModel instance;

//    private static final String DB_HOST = "localhost";
//    private static final String DB_PORT = "3306";
//    private static final String DB_NAME = "perpustakaan";
//    private static final String DB_USERNAME = "root";
//    private static final String DB_PASSWORD = "";
//
//    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
//    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;

    private static final String FIELD_KODE = "kdBuku";
    private static final String FIELD_JUDUL = "judulBuku";
    private static final String FIELD_PENGARANG = "namaPengarang";
    private static final String FIELD_PENERBIT = "namaPenerbit";
    private static final String FIELD_JUMLAH = "jumlahBuku";
    private static final String FIELD_KATEGORI = "kategoriBuku";
    private static final String FIELD_EDISI = "edisi";
    private static final String FIELD_TERBIT = "tanggalTerbit";
    private static final String FIELD_LETAK = "letakBuku";
    private static final String FIELD_MASUK = "tanggalMasuk";



//    private Connection conn = null;
//    private PreparedStatement statement = null;
    
    AllModel all = new AllModel();
    
    List<Buku> listBuku;

    public static BukuModel getInstance() throws SQLException {
        if (instance == null) {
            instance = new BukuModel();
        }
        return instance;
    }

    public BukuModel() throws SQLException {
        all.bukaKonDB();
    }

    
    
    public Buku findBuku(String kdBuku) throws SQLException{
        Buku buku = null;
        String sql = "SELECT * FROM tbl_buku,tbl_pengarang,tbl_penerbit Where kdBuku = ?";
        System.out.println("\nFinding record...");
        all.statement = all.conn.prepareStatement(sql);
        all.statement.setString(1,kdBuku);
        
        ResultSet rs = all.statement.executeQuery();
        
        while(rs.next()){
            buku = new Buku();
            buku.setKdBuku(rs.getString(FIELD_KODE));
            buku.setJudulBuku(rs.getString(FIELD_JUDUL));
            buku.setPengarangBuku(rs.getString(FIELD_PENGARANG));
            buku.setPenerbitBuku(rs.getString(FIELD_PENERBIT));
            buku.setJumlahBuku(rs.getInt((FIELD_JUMLAH)));
            buku.setKategoriBuku(rs.getString(FIELD_KATEGORI));
            buku.setLetakBuku(rs.getString(FIELD_LETAK));
//            buku.setTanggalMasuk(rs.getString(FIELD_MASUK));
            System.out.println("Record Found: KDBUKU = "+buku.getKdBuku());
        }
        rs.close();
        all.statement.close();
        return buku;
    }
    
    public int insertBuku(Buku buku) throws SQLException{
        String sql = "INSERT INTO tbl_buku"
                + "(kdBuku,judulBuku,pengarangBuku,penerbitBuku,jumlahBuku,kategoriBuku,letakBuku)"
                + "values (?,?,?,?,?,?,?);";
        System.out.println("Inserting Data....");
        all.statement = all.conn.prepareStatement(sql);
        all.statement.setString(1, buku.getKdBuku());
        all.statement.setString(2, buku.getJudulBuku());
        all.statement.setString(3, buku.getPengarangBuku());
        all.statement.setString(4, buku.getPenerbitBuku());
        all.statement.setString(5, String.valueOf(buku.getJumlahBuku()));
        all.statement.setString(6, buku.getKategoriBuku());
        all.statement.setString(7, buku.getLetakBuku());
//        statement.setString(8, String.valueOf(buku.getTanggalMasuk()));
        
        int result = all.statement.executeUpdate();
        all.statement.close();
        System.out.println(String.valueOf(result)+" new record created");
        return result;
    }
    
    public int updateBuku(Buku buku) throws SQLException{
        String sql =  "UPDATE tbl_buku SET"
                + " judulBuku=?,pengarangBuku=?,penerbitBuku=?,jumlahBuku=?,kategoriBuku=?,letakBuku=? "
                + "where kdbuku=?;";
        System.out.println("Inserting Data....");
        all.statement = all.conn.prepareStatement(sql);
        all.statement.setString(1, buku.getJudulBuku());
        all.statement.setString(2, buku.getPengarangBuku());
        all.statement.setString(3, buku.getPenerbitBuku());
        all.statement.setString(4, String.valueOf(buku.getJumlahBuku()));
        all.statement.setString(5, buku.getKategoriBuku());
        all.statement.setString(6, buku.getLetakBuku());
        all.statement.setString(7, buku.getKdBuku());
        
//        statement.setString(8, String.valueOf(buku.getTanggalMasuk()));
        
        int result = all.statement.executeUpdate();
        all.statement.close();
        System.out.println(String.valueOf(result)+" update record sucsessfull");
        return result;
    }
    
    public int deleteBuku(String param) throws SQLException{
        String sql = "DELETE FROM tbl_buku Where kdBuku = ?";
        all.statement = all.conn.prepareStatement(sql);
        all.statement.setString(1, param);
        int result = all.statement.executeUpdate();
        all.statement.close();
        
        return result;
    } 
    
//    public void listPenerbit() throws SQLException{
//        
//        
//    }
            
            
    public List<Buku> getListBuku(String param) throws SQLException {
        listBuku = new ArrayList<>();
        Buku buku = null;
        String sql;

        System.out.println("Executing query..");
        if (param == null || param.isEmpty()) {
            sql = "select b.kdBuku, b.judulBuku, pen.namaPengarang,p.namaPenerbit,b.letakBuku FROM tbl_buku as b, tbl_penerbit as p, tbl_pengarang as pen WHERE b.pengarangBuku = pen.idPengarang and b.penerbitBuku = p.idPenerbit and jumlahBuku > 0";
            all.statement = all.conn.prepareStatement(sql);
        } 
        else {
//            sql = "select b.kdBuku, b.judulBuku, pen.namaPengarang,p.namaPenerbit,b.letakBuku FROM tbl_buku as b, tbl_penerbit as p, tbl_pengarang as pen WHERE b.pengarangBuku = pen.idPengarang and b.penerbitBuku = p.idPenerbit"
//                    + "OR kdBuku LIKE ? OR UPPER(b.judulbuku) LIKE UPPER(?) OR"
//                    + " UPPER(pen.namaPengarang) LIKE UPPER(?) OR UPPER(p.namaPenerbit) LIKE UPPER(?)"
//                    + "OR UPPER(b.letakBuku) LIKE UPPER(?)";
            sql = "select b.kdBuku, b.judulBuku, pen.namaPengarang,p.namaPenerbit,b.letakBuku FROM tbl_buku as b, tbl_penerbit as p, tbl_pengarang as pen WHERE kdBuku LIKE ? OR UPPER(b.judulbuku) LIKE UPPER(?) OR"
                    + " UPPER(pen.namaPengarang) LIKE UPPER(?) OR UPPER(p.namaPenerbit) LIKE UPPER(?)"
                    + "OR UPPER(b.letakBuku) LIKE UPPER(?)";
            
            all.statement = all.conn.prepareStatement(sql);
            all.statement.setString(1, param);
            all.statement.setString(2, param);
            all.statement.setString(3, param);
            all.statement.setString(4, param);
            all.statement.setString(5, param);
        }

        ResultSet rs = all.statement.executeQuery();
        while (rs.next()) {
            buku = new Buku();
            buku.setKdBuku(rs.getString(FIELD_KODE));
            buku.setJudulBuku(rs.getString(FIELD_JUDUL));
            buku.setPengarangBuku(rs.getString(FIELD_PENGARANG));
            buku.setPenerbitBuku(rs.getString(FIELD_PENERBIT));
            buku.setLetakBuku(rs.getString(FIELD_LETAK));
            listBuku.add(buku);
        }
        rs.close();
        all.statement.close();
        System.out.println("Found" + listBuku.size() + " record.");
        return listBuku;
    }

    public List<Buku> getListBuku() throws SQLException {
        return getListBuku(null);
    }

    
    
//    private Connection bukaKonDB() throws SQLException {
//        try {
//            Class.forName(JDBC_DRIVER);
//            System.out.println("Connecting to Database..");
//            conn = (Connection) DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
//            if (conn != null) {
//                System.out.println("Successfully connected to datebase.");
//            }
//            return conn;
//        } catch (ClassNotFoundException ex) {
//            ex.printStackTrace();
//        }
//
//        return null;
//    }
//
//    public void tutupKonDB() {
//        try {
//            if (statement != null && !statement.isClosed()) {
//                statement.close();
//                System.out.println("Statement closed");
//            }
//            if (conn != null && !conn.isClosed()) {
//                conn.close();
//                System.out.println("Connection closed");
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//    }
}
