/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uas.siprus.model;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import uas.siprus.Anggota;

/**
 *
 * @author null
 */
public class AnggotaDataTable extends AbstractTableModel{
    private static final String[] columnNames = {"Kode Anggota","Nama Anggota","Pekerjaan"};
    
    public static final int KOLOM_KODE = 0;
    public static final int KOLOM_NAMA = 1;
    public static final int KOLOM_PEKERJAAN = 2;
    
    List<Anggota> data;
    
    public AnggotaDataTable(List<Anggota> data){
        super();
        this.data = data;
    }
    
    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Anggota anggota = data.get(rowIndex);
        switch(columnIndex){
            case KOLOM_KODE:
                return anggota.getKdAnggota();
            case KOLOM_NAMA:
                return anggota.getNamaAnggota();
            case KOLOM_PEKERJAAN:
                return anggota.getPekerjaanAnggota();
        }
        return null;
    }
    
    public String getColumnName(int column){
        return columnNames[column];
    }
}
