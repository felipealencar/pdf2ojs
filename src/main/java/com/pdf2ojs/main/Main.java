/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdf2ojs.main;

import com.pdf2ojs.view.ExtractorView;

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
