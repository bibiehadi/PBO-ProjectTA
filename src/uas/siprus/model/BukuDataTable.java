/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uas.siprus.model;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import uas.siprus.Bacaan;
import uas.siprus.Buku;

/**
 *
 * @author null
 */
public class BukuDataTable extends AbstractTableModel {

    private static final String[] columnNames = {"Kode Buku", "Judul Buku", "Pengarang", "Penerbit", "Letak"};

    public static final int KOLOM_KODE = 0;
    public static final int KOLOM_JUDUL = 1;
    public static final int KOLOM_PENGARANG = 2;
    public static final int KOLOM_PENERBIT = 3;
    public static final int KOLOM_LETAK = 4;

    List<Buku> data;

    public BukuDataTable(List<Buku> data) {
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
        Buku buku = data.get(rowIndex);
        switch(columnIndex){
            case KOLOM_KODE:
                return buku.getKdBuku();
            case KOLOM_JUDUL:
                return buku.getJudulBuku();
            case KOLOM_PENGARANG:
                return buku.getPengarangBuku();
            case KOLOM_PENERBIT:
                return buku.getPenerbitBuku();
            case KOLOM_LETAK:
                return buku.getLetakBuku();
        }
        return null;
    }
    
    public String getColumnName(int column){
        return columnNames[column];
    }
}
