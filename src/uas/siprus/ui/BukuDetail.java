/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uas.siprus.ui;

import java.awt.CardLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import uas.siprus.Buku;
import uas.siprus.SIPRUS;
import uas.siprus.model.BukuModel;

/**
 *
 * @author null
 */
public class BukuDetail extends javax.swing.JInternalFrame {

    static BukuDetail instance;
    BukuModel bukuModel;
    String kdTampil;
    int modeForm;

//    List<String> db ;
    private Connection conn;
    private PreparedStatement statement;

    public static final int MODE_TAMPIL = 0;
    public static final int MODE_EDIT = 1;
    public static final int MODE_TAMBAH = 2;

    /**
     * Creates new form BukuDetail
     *
     * @return
     */
    public static BukuDetail getInstance() {
        if (instance == null) {
            instance = new BukuDetail();
        }
        return instance;
    }

    public BukuDetail() {
        initComponents();
        dataComboBox();
        this.modeForm = 0;
        
        this.imageChooser.setCurrentDirectory(new File(SIPRUS.DIR_FOTO_DUMMY));
        imageChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "png", "gif"));
        
        try {
            bukuModel = BukuModel.getInstance();
        } catch (SQLException ex) {
            Logger.getLogger(BukuDetail.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void tampilDetailBuku(String kdTampil) {
        Buku buku = new Buku();
        try {
            buku = bukuModel.findBuku(kdTampil);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "SQL ERROR");
            ex.printStackTrace();
        }
        labelKD.setText(buku.getKdBuku());
        kdBuku.setText(buku.getKdBuku());

        labelJudul.setText(buku.getJudulBuku());
        judulBuku.setText(buku.getJudulBuku());

        labelKategori.setText(buku.getKategoriBuku());
        kategori.setText(buku.getKategoriBuku());

        labelPengarang.setText(buku.getPengarangBuku());
        pengarang.setSelectedItem(buku.getPengarangBuku());

        labelPenerbit.setText(buku.getPenerbitBuku());
        penerbit.setSelectedItem(buku.getPenerbitBuku());

        labelLetak.setText(buku.getLetakBuku());
        letak.setText(buku.getLetakBuku());

        labelJumlah.setText(String.valueOf(buku.getJumlahBuku()));
        jumlah.setSelectedItem(String.valueOf(buku.getJumlahBuku()));

        tampilkanGambarBuku(buku.getKdBuku());
        this.aktifkanModeForm(MODE_TAMPIL);
        this.setVisible(true);

    }
    
    private void tampilkanGambarBuku(String kdbuku){
       labelFoto.setIcon(new ImageIcon(SIPRUS.FOTO_DEFAULT_URL));
       
       String urlGambar = SIPRUS.DIR_FOTO + kdbuku + ".jpg";
       File fileGambar = new File(urlGambar);
       ImageIcon gambar;
       
        if (fileGambar.isFile()) {
            gambar = new ImageIcon(urlGambar);
            gambar.getImage().flush();
            
            labelFoto.setIcon(gambar);
        }
    }
    
    private void simpanGambarBuku(String kdbuku){
        String urlGambarAsli = SIPRUS.DIR_FOTO+ kdbuku +".jpg";
        String urlGambarTemp = urlGambarAsli + ".bak";
        
        File gambarUpload = imageChooser.getSelectedFile();
        
        File gambarLama = new File(urlGambarAsli);
        File gambarTemp = new File(urlGambarTemp);
        
        if (gambarUpload != null) {
            if (gambarLama.isFile()) {
                gambarLama.renameTo(gambarTemp);
            }
            
            FileOutputStream outputStream;
            
            try{
                if (gambarUpload.isFile()){
                    Path path = Paths.get(gambarUpload.getAbsolutePath());
                    byte[] data = Files.readAllBytes(path);
                    
                    outputStream = new FileOutputStream(urlGambarAsli);
                    outputStream.write(data);
                    outputStream.close();
                }
            }catch (FileNotFoundException ex){
                ex.printStackTrace();
            }catch (IOException ex){
                ex.printStackTrace();
            }
            
            if(gambarTemp.isFile()){
                gambarTemp.delete();
            }
            
        }
    }
    
    public void aktifkanModeForm(int modForm) {
        this.modeForm = modForm;
        aturUlangForm();

        this.setVisible(true);
    }

    private void aturUlangForm() {
        CardLayout formLayout = (CardLayout) formPanel.getLayout();
        if (this.modeForm == MODE_TAMPIL) {
            formLayout.show(formPanel, "detail");

            ubahButton.setEnabled(true);
            simpanButton.setEnabled(false);
            resetButton.setEnabled(false);
            labelKlikFoto.setVisible(false);
        } else {
            formLayout.show(formPanel, "edit");
            ubahButton.setEnabled(false);
            deleteButton.setEnabled(false);
            simpanButton.setEnabled(true);
            resetButton.setEnabled(true);
            labelKlikFoto.setVisible(true);
            kdBuku.setEditable(false);

            if (this.modeForm == MODE_TAMBAH) {
                kdBuku.setText("");
                kdBuku.setEditable(true);
                kdBuku.requestFocus();
                judulBuku.setText("");
                pengarang.setSelectedIndex(0);
                penerbit.setSelectedIndex(0);
                kategori.setText("");
                jumlah.setSelectedItem(0);
                labelFoto.setIcon(null);
            }
        }
    }

    private void dataComboBox() {
//        BukunonKoran buku = new BukunonKoran();
//        try {
//            BukuModel bukuModel = BukuModel.getInstance();
//            String sql = "SELECT namaPengarang FROM tbl_pengarang";
//            statement = conn.prepareStatement(sql);
//            ResultSet rs = statement.executeQuery(sql);
//            while (rs.next()) {
//                pengarang.addItem(rs.getString("namaPengarang"));
//                System.out.println(rs.getString("idPengarang"));
//            }
//            rs.close();
//            statement.close();
//        } catch (SQLException ex) {
//            ex.printStackTrace();
////            JOptionPane.showMessageDialog(this, "SQL ERROR: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//        
//        
//        try {
//            String sql = "SELECT * FROM tbl_penerbit";
//            statement = conn.prepareStatement(sql);
//            ResultSet rs = statement.executeQuery();
//            while (rs.next()) {
//                penerbit.addItem(rs.getString("idPenerbit"));
//            }
//            rs.close();
//            statement.close();
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//                JOptionPane.showMessageDialog(this, "SQL ERROR: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
        for (int i = 0; i <= 50; i++) {
            jumlah.addItem(String.valueOf(i));
        }

        for (int i = 1; i <= 31; i++) {
            tanggal.addItem(i);
        }
        int tahunSekarang = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1999; i < tahunSekarang; i++) {
            tahun.addItem(i);
        }

        bulan.addItem("Januari");
        bulan.addItem("Februari");
        bulan.addItem("Maret");
        bulan.addItem("April");
        bulan.addItem("Mei");
        bulan.addItem("Juni");
        bulan.addItem("Juli");
        bulan.addItem("Agustus");
        bulan.addItem("September");
        bulan.addItem("Oktober");
        bulan.addItem("November");
        bulan.addItem("Desember");

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        imageChooser = new javax.swing.JFileChooser();
        mainPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        formPanel = new javax.swing.JPanel();
        detail = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        labelPenerbit = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        labelTanggal = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        labelPengarang = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        labelKategori = new javax.swing.JLabel();
        labelKD = new javax.swing.JLabel();
        labelJumlah = new javax.swing.JLabel();
        labelJudul = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        labelLetak = new javax.swing.JLabel();
        edit = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        kdBuku = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        judulBuku = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        pengarang = new javax.swing.JComboBox<>();
        jLabel32 = new javax.swing.JLabel();
        penerbit = new javax.swing.JComboBox<>();
        jLabel34 = new javax.swing.JLabel();
        kategori = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        letak = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        tanggal = new javax.swing.JComboBox();
        bulan = new javax.swing.JComboBox();
        tahun = new javax.swing.JComboBox();
        jLabel37 = new javax.swing.JLabel();
        jumlah = new javax.swing.JComboBox<>();
        buttonPanel = new javax.swing.JPanel();
        tutupButton = new javax.swing.JButton();
        ubahButton = new javax.swing.JButton();
        simpanButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        fotoPanel = new javax.swing.JPanel();
        labelFoto = new javax.swing.JLabel();
        labelKlikFoto = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Detail Buku");

        formPanel.setLayout(new java.awt.CardLayout());

        jLabel3.setText("Judul");

        labelPenerbit.setText("Penerbit");

        jLabel11.setText("Pengarang");

        jLabel2.setText("Kode Buku");

        jLabel7.setText("Kategori");

        labelTanggal.setText("Tanggal Masuk");

        jLabel12.setText("Penerbit");

        jLabel8.setText("Letak");

        labelPengarang.setText("Pengarang");

        jLabel13.setText("Jumlah Buku");

        labelKategori.setText("Kategori");

        labelKD.setText("Kode Buku");

        labelJumlah.setText("Jumlah Buku");

        labelJudul.setText("Judul");

        jLabel9.setText("Tanggal Masuk");

        labelLetak.setText("Letak");

        javax.swing.GroupLayout detailLayout = new javax.swing.GroupLayout(detail);
        detail.setLayout(detailLayout);
        detailLayout.setHorizontalGroup(
            detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel7)
                    .addComponent(jLabel13)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addGap(28, 28, 28)
                .addGroup(detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(detailLayout.createSequentialGroup()
                        .addGroup(detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(labelKD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(labelJudul, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(labelPengarang, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                                .addComponent(labelPenerbit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(labelKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelLetak, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(detailLayout.createSequentialGroup()
                        .addComponent(labelTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(51, Short.MAX_VALUE))))
        );
        detailLayout.setVerticalGroup(
            detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelKD)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelJudul, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(detailLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(labelPengarang))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, detailLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)))
                .addGap(18, 18, 18)
                .addGroup(detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelPenerbit)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelKategori)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(labelJumlah))
                .addGap(18, 18, 18)
                .addGroup(detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(labelLetak, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(labelTanggal))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        formPanel.add(detail, "detail");

        jLabel33.setText("Kode Buku");

        jLabel30.setText("Judul Buku");

        jLabel31.setText("Pengarang");

        pengarang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01" }));
        pengarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pengarangActionPerformed(evt);
            }
        });

        jLabel32.setText("Penerbit");

        penerbit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01" }));

        jLabel34.setText("Kategori");

        jLabel35.setText("Jumlah");

        jLabel36.setText("Tanggal Masuk");

        tahun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tahunActionPerformed(evt);
            }
        });

        jLabel37.setText("Letak");

        javax.swing.GroupLayout editLayout = new javax.swing.GroupLayout(edit);
        edit.setLayout(editLayout);
        editLayout.setHorizontalGroup(
            editLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(editLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(editLayout.createSequentialGroup()
                        .addGroup(editLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33)
                            .addComponent(jLabel30)
                            .addComponent(jLabel31)
                            .addComponent(jLabel32))
                        .addGap(71, 71, 71)
                        .addGroup(editLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(kdBuku, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(judulBuku, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(pengarang, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(penerbit, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(editLayout.createSequentialGroup()
                        .addGroup(editLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34)
                            .addComponent(jLabel35)
                            .addComponent(jLabel36)
                            .addComponent(jLabel37))
                        .addGap(52, 52, 52)
                        .addGroup(editLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(letak)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bulan, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tahun, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(kategori, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jumlah, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        editLayout.setVerticalGroup(
            editLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(editLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(kdBuku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(editLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(judulBuku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(editLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(pengarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(editLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(penerbit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(editLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(kategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(editLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(editLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(letak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(editLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tahun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        formPanel.add(edit, "edit");

        tutupButton.setText("Tutup");
        tutupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tutupButtonActionPerformed(evt);
            }
        });

        ubahButton.setText("Ubah Data");
        ubahButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ubahButtonActionPerformed(evt);
            }
        });

        simpanButton.setText("Simpan");
        simpanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanButtonActionPerformed(evt);
            }
        });

        resetButton.setText("Reset");

        deleteButton.setText("Hapus");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ubahButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(simpanButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resetButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 125, Short.MAX_VALUE)
                .addComponent(tutupButton)
                .addContainerGap())
        );
        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonPanelLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ubahButton)
                    .addComponent(simpanButton)
                    .addComponent(resetButton)
                    .addComponent(tutupButton)
                    .addComponent(deleteButton))
                .addContainerGap())
        );

        labelFoto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        labelFoto.setMaximumSize(new java.awt.Dimension(100, 100));
        labelFoto.setMinimumSize(new java.awt.Dimension(100, 100));
        labelFoto.setPreferredSize(new java.awt.Dimension(100, 100));
        labelFoto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelFotoMouseClicked(evt);
            }
        });
        fotoPanel.add(labelFoto);

        labelKlikFoto.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        labelKlikFoto.setText("Klik foto untuk mengubah");
        fotoPanel.add(labelKlikFoto);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addComponent(fotoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(mainPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel1)
                    .addContainerGap(396, Short.MAX_VALUE)))
            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(mainPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(formPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(141, Short.MAX_VALUE)))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(fotoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(mainPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel1)
                    .addContainerGap(352, Short.MAX_VALUE)))
            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                    .addContainerGap(45, Short.MAX_VALUE)
                    .addComponent(formPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(68, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tahunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tahunActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tahunActionPerformed

    private void pengarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pengarangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pengarangActionPerformed

    private void tutupButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tutupButtonActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_tutupButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        try {
            // TODO add your handling code here:
            BukuModel bukuModel = BukuModel.getInstance();
            System.out.println(labelKD.getText());
            bukuModel.deleteBuku(labelKD.getText());
        } catch (SQLException ex) {
            Logger.getLogger(BukuDetail.class.getName()).log(Level.SEVERE, null, ex);

        }
        JOptionPane.showMessageDialog(this, "Data telah berhasil dihapus.", "Simpan Data", JOptionPane.INFORMATION_MESSAGE);
        this.setVisible(false);

    }//GEN-LAST:event_deleteButtonActionPerformed

    private void simpanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanButtonActionPerformed
        // TODO add your handling code here:
        if (validasiIsiForm()) {

            Buku bukuUpdate = new Buku();

            bukuUpdate.setKdBuku(kdBuku.getText());
            bukuUpdate.setJudulBuku(judulBuku.getText());
            bukuUpdate.setPenerbitBuku(String.valueOf(penerbit.getSelectedIndex() + 1));
            bukuUpdate.setPengarangBuku(String.valueOf(pengarang.getSelectedIndex() + 1));
            bukuUpdate.setKategoriBuku(kategori.getText());
            bukuUpdate.setJumlahBuku(jumlah.getSelectedIndex());
            bukuUpdate.setLetakBuku(letak.getText());
//        bukuUpdate.setTanggalMasuk(tanggal.getSelectedItem().toString(),
//                String.format("%02d",bulan.getSelectedIndex()+1),tahun.getSelectedItem().toString());

            Buku buku = new Buku();
            try {
                buku = bukuModel.findBuku(bukuUpdate.getKdBuku());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            if (buku == null) {
                try {
                    bukuModel.insertBuku(bukuUpdate);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "SQL ERROR: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                JOptionPane.showMessageDialog(this, "Data telah berhasil disimpan.", "Simpan Data", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int a = JOptionPane.showConfirmDialog(this, "Buku dengan Kode Buku " + bukuUpdate.getKdBuku() + " sudah ada. \n"
                        + "Apakah Anda ingin mengubah data buku tersebut?", "Simpan Data", JOptionPane.YES_NO_OPTION);
                if (a == JOptionPane.YES_OPTION) {
                    try {
                        bukuModel.updateBuku(bukuUpdate);
                        JOptionPane.showMessageDialog(this, "Data telah berhasil disimpan.", "Simpan Data", JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "SQL ERROR: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            simpanGambarBuku(bukuUpdate.getKdBuku());
            tampilDetailBuku(bukuUpdate.getKdBuku());
            BukuFrame.getInstance().tampilkanListBuku(null);
        }
        
        
        
    }//GEN-LAST:event_simpanButtonActionPerformed

    private void ubahButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ubahButtonActionPerformed
        // TODO add your handling code here:
        this.aktifkanModeForm(MODE_EDIT);
    }//GEN-LAST:event_ubahButtonActionPerformed

    private void labelFotoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelFotoMouseClicked
        // TODO add your handling code here:
        if (this.modeForm != MODE_TAMPIL) {
            if (imageChooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
                labelFoto.setIcon(new ImageIcon(imageChooser.getSelectedFile().getAbsolutePath()));
            }
        }
    }//GEN-LAST:event_labelFotoMouseClicked

    private boolean validasiIsiForm() {
        boolean isValid = true;
        String pesan = "Silahkan periksa kembali kesalahan berikut :";

        if (kdBuku.getText().equals("")) {
            pesan += "\n - Kode buku harus diisi ";
            isValid = false;
        }

        if (judulBuku.getText().equals("")) {
            pesan += "\n - Judul buku harus diisi ";
            isValid = false;
        }
        if (letak.getText().equals("")) {
            pesan += "\n - Letak buku harus diisi ";
            isValid = false;
        }
        if (kategori.getText().equals("")) {
            pesan += "\n - Kategori buku harus diisi ";
            isValid = false;
        }

        if (!isValid) {
            JOptionPane.showMessageDialog(this, pesan, "Error Form", JOptionPane.ERROR_MESSAGE);
        }

        return isValid;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox bulan;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JPanel detail;
    private javax.swing.JPanel edit;
    private javax.swing.JPanel formPanel;
    private javax.swing.JPanel fotoPanel;
    private javax.swing.JFileChooser imageChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField judulBuku;
    private javax.swing.JComboBox<String> jumlah;
    private javax.swing.JTextField kategori;
    private javax.swing.JTextField kdBuku;
    private javax.swing.JLabel labelFoto;
    private javax.swing.JLabel labelJudul;
    private javax.swing.JLabel labelJumlah;
    private javax.swing.JLabel labelKD;
    private javax.swing.JLabel labelKategori;
    private javax.swing.JLabel labelKlikFoto;
    private javax.swing.JLabel labelLetak;
    private javax.swing.JLabel labelPenerbit;
    private javax.swing.JLabel labelPengarang;
    private javax.swing.JLabel labelTanggal;
    private javax.swing.JTextField letak;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JComboBox<String> penerbit;
    private javax.swing.JComboBox<String> pengarang;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton simpanButton;
    private javax.swing.JComboBox tahun;
    private javax.swing.JComboBox tanggal;
    private javax.swing.JButton tutupButton;
    private javax.swing.JButton ubahButton;
    // End of variables declaration//GEN-END:variables
}
