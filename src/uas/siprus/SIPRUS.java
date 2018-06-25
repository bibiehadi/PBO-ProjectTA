/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uas.siprus;

import java.nio.file.Paths;
import uas.siprus.ui.MainFrame;

/**
 *
 * @author null
 */
public class SIPRUS {

    public static final String DIR_APLIKASI = Paths.get("").toAbsolutePath().toString();
    public static final String DIR_FOTO = DIR_APLIKASI + "\\photos\\";
    public static final String DIR_FOTO_DUMMY = DIR_APLIKASI + "\\dummyphotos\\";
    public static final String FOTO_DEFAULT_URL = DIR_FOTO + "default.jpg";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = MainFrame.getInstance();
                
                mainFrame.setVisible(true);
            }
        }
        );

    }
}
