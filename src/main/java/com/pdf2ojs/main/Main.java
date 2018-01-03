/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sbc.sbceventpdfextractor.main;

import com.sbc.sbceventpdfextractor.view.ExtractorView;

/**
 *
 * @author gustavo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ExtractorView ev = new ExtractorView();
                ev.setVisible(true);
            }
        });
    }
    
}
