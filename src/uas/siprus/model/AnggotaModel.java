/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uas.siprus.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import uas.siprus.Anggota;

/**
 *
 * @author null
 */
public class AnggotaModel extends AllModel {

    static AnggotaModel instance;

//    private static final String DB_HOST = "localhost";
//    private static final String DB_PORT = "3306";
//    private
    AllModel all = new AllModel();
    List<Anggota> listAnggota;

    public static AnggotaModel getInstance() throws SQLException {
        if (instance == null) {
            instance = new AnggotaModel();
        }
        return instance;
    }

    public AnggotaModel() throws SQLException {
        all.bukaKonDB();
    }

    public Anggota findAnggota(String kdAnggota) throws SQLException {
        Anggota anggota = null;
        String sql = "SELECT * FROM tbl_Anggota WHERE kdAnggota = ?";
        System.out.println("\nFinding record..");
        all.statement = all.conn.prepareStatement(sql);
        all.statement.setString(1, kdAnggota);
        
        ResultSet rs = all.statement.executeQuery();
        while(rs.next()){
            anggota = new Anggota();
            anggota.setKdAnggota(rs.getString("kdAnggota"));
            anggota.setNamaAnggota(rs.getString("namaAnggota"));
            anggota.setAlamatAnggota(rs.getString("alamatAnggota"));
            anggota.setPekerjaanAnggota(rs.getString("pekerjaanAnggota"));
            anggota.setJkAnggota(rs.getString("jkAnggota"));
//            anggota.setTanggallahirAnggota(rs.getDate("tanggallahirAnggota"));
            anggota.setNoTelp(rs.getString("noTelp"));
            System.out.println("Record Fond : Kode Anggota = "+anggota.getKdAnggota());
        }
        rs.close();
        all.statement.close();
        return anggota;
    }

    public int insertAnggota(Anggota anggota) throws SQLException{
        String sql = "INSERT INTO tbl_anggota"
                + "(kdAnggota,namaAnggota,alamatAnggota,jkAnggota,pekerjaanAnggota,noTelp)"
                + " values (?,?,?,?,?,?);";
        
        System.out.println("Inserting Data..");
        all.statement = all.conn.prepareStatement(sql);
        all.statement.setString(1, anggota.getKdAnggota());
        all.statement.setString(2, anggota.getNamaAnggota());
        all.statement.setString(3, anggota.getAlamatAnggota());
        all.statement.setString(4, anggota.getJkAnggota());
//        all.statement.setDate(5, (Date) anggota.getTanggallahirAnggota());
        all.statement.setString(5, anggota.getPekerjaanAnggota());
        all.statement.setString(6, anggota.getNoTelp());
        
        int result = all.statement.executeUpdate();
        all.statement.close();
        System.out.println(String.valueOf(result)+" new record created");
        return result;
    }
//    
//    public int updateAnggota(Anggota anggota) throws SQLException{
//        String sql = "UPDATE tbl_buku SET "
//                + " namaAnggota=?,alamatAnggota=?,jkAnggota=?,"
//    }
    
    public int updateAnggota(Anggota anggota) throws SQLException{
        String sql = "UPDATE tbl_anggota "
                + "SET namaAnggota=?,alamatAnggota=?,jkAnggota=?,pekerjaanAnggota=?,noTelp=?"
                + " where kdAnggota=?;";
        System.out.println("\nUpdating record..");
        all.statement = all.conn.prepareStatement(sql);
        all.statement.setString(1, anggota.getNamaAnggota());
        all.statement.setString(2, anggota.getAlamatAnggota());
        all.statement.setString(3, anggota.getJkAnggota());
//        all.statement.setDate(5, (Date) anggota.getTanggallahirAnggota());
        all.statement.setString(4, anggota.getPekerjaanAnggota());
        all.statement.setString(5, anggota.getNoTelp());
        all.statement.setString(6, anggota.getKdAnggota());
        int result = all.statement.executeUpdate();
        all.statement.close();
        return result;
    }
    
    public int deleteAnggota(String param) throws SQLException{
        String sql = "DELETE FROM tbl_anggota'' WHERE kdAnggota = ?"; 
        all.statement = all.conn.prepareStatement(sql);
        all.statement.setString(1,param);
        int result = all.statement.executeUpdate();
        all.statement.close();
        
        return result;
    }
    
    
    
    
    
    public List<Anggota> getListAnggota(String param) throws SQLException {
        listAnggota = new ArrayList<>();
        Anggota anggota = null;
        String sql;

        System.out.println("Executing query..");
        if (param == null || param.isEmpty()) {
            sql = "SELECT kdAnggota, namaAnggota, pekerjaanAnggota FROM tbl_anggota";
            all.statement = all.conn.prepareStatement(sql);
        } else {

        }
        ResultSet rs = all.statement.executeQuery();
        while (rs.next()) {
            anggota = new Anggota();
            anggota.setKdAnggota(rs.getString("kdAnggota"));
            System.out.println(rs.getString("kdAnggota"));
            anggota.setNamaAnggota(rs.getString("namaAnggota"));
//            anggota.setAlamatAnggota(rs.getString("alamatAnggota"));
            anggota.setPekerjaanAnggota(rs.getString("pekerjaanAnggota"));
//            anggota.setJkAnggota(rs.getString("jkAnggota"));
//            anggota.setTanggallahirAnggota(rs.getDate("tanggallahirAnggota"));
            listAnggota.add(anggota);
        }
        rs.close();
        all.statement.close();
        return listAnggota;
    }

}
