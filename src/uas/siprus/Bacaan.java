/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uas.siprus;

//import java.sql.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * @author null
 */
public class Bacaan {
    private String kdBuku;
    private String judulBuku;
    private String penerbitBuku;
    private int jumlahBuku;
    private Date tanggalMasuk;
    private String letakBuku;

    SimpleDateFormat inputDateFormat;
    SimpleDateFormat dbDateFormat;
    SimpleDateFormat outputDateFormat;
    
    public String getKdBuku() {
        return kdBuku;
    }

    public void setKdBuku(String kdBuku) {
        this.kdBuku = kdBuku;
    }

    public String getJudulBuku() {
        return judulBuku;
    }

    public void setJudulBuku(String judulBuku) {
        this.judulBuku = judulBuku;
    }

    public String getPenerbitBuku() {
        return penerbitBuku;
    }

    public void setPenerbitBuku(String penerbitBuku) {
        this.penerbitBuku = penerbitBuku;
    }

    public int getJumlahBuku() {
        return jumlahBuku;
    }

    public void setJumlahBuku(int jumlahBuku) {
        this.jumlahBuku = jumlahBuku;
    }

    public Date getTanggalMasuk() {
        return tanggalMasuk;
    }

    public String getTanggalMasukStr(){
        return outputDateFormat.format(tanggalMasuk);
    }
    
    public String getTanggalMasukDB(){
        return dbDateFormat.format(tanggalMasuk);
    }
    
    public void setTanggalMasuk(Date tanggalMasuk) {
        this.tanggalMasuk = tanggalMasuk;
    }
    
    public void setTanggalMasuk(String tanggal,String bulan,String tahun){
        String strMasuk = tanggal+"/"+bulan+"/"+tahun;
        try{
            this.tanggalMasuk = inputDateFormat.parse(strMasuk);
        }catch (ParseException ex){
            this.tanggalMasuk = new Date();
        }
    }

    public String getLetakBuku() {
        return letakBuku;
    }

    public void setLetakBuku(String letakBuku) {
        this.letakBuku = letakBuku;
    }
}
