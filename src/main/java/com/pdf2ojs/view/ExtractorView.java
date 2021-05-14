/* 
 * Copyright (C) 2018 gustavo
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.pdf2ojs.view;

import com.pdf2ojs.model.ArticleMeta;
import com.pdf2ojs.model.ExportOjs;
import com.pdf2ojs.model.ExtractPdf;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import pl.edu.icm.cermine.exception.AnalysisException;

/**
 *
 * @author gustavo
 */
public class ExtractorView extends javax.swing.JFrame
{
    private String system = System.getProperty( "os.name" );
    private String userHome = System.getProperty( "user.home" ) + "\\";
    private File[] papers;
    private String xmlPath;
    private ExtractorView parent = this;
    List<ArticleMeta> metapapers = new ArrayList<ArticleMeta>();
    
    public ExtractorView()
    {
        this.setTitle( "PDF to OJS" );
        initComponents();
        filesTextField.setEditable( false );
        xmlTextField.setEditable( false );
        extractButton.setEnabled( false );
        extractProgressBar.setForeground( Color.BLUE );
        extractProgressBar.setStringPainted( true );
        extractProgressBar.setValue( 0 );
        sectionsButton.setVisible( false );
    }

    private void openFileChooser()
    {
        JFileChooser file = new JFileChooser();
        file.setFileSelectionMode( JFileChooser.FILES_ONLY );
        file.setMultiSelectionEnabled( true );
        int i = file.showSaveDialog( null );
        
        if ( i == 1 )
        {
            filesTextField.setText( "" );
        }
        else
        {
            papers = file.getSelectedFiles();
            filesTextField.setText( papers.length + " files selected" );
            extractButton.setEnabled( true );
            extractProgressBar.setValue( 0 );
            extractProgressBar.setString( "0%" );
        }
    }

    private void openFileSave()
    {
        JFileChooser chooser = new JFileChooser();
        int retrival = chooser.showSaveDialog( null );
        if ( retrival == JFileChooser.APPROVE_OPTION )
        {
            try
            {
                xmlPath = chooser.getSelectedFile().getAbsolutePath();
                xmlTextField.setText( xmlPath );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
    }

    public void setFields( boolean enable )
    {
        issueDateTextField.setEditable( enable );
        issueTittelTextField.setEditable( enable );
        issueYearTextField.setEditable( enable );
        fileChooserButton.setEnabled( enable );
        fileSaveButton.setEnabled( enable );
        addButton.setEnabled( enable );
        editButton.setEnabled( enable );
        saveButton.setEnabled( enable );
        extractButton.setEnabled( enable );
    }
    
    private void generateTempFiles()
    {
        if ( system != null )
        {
            if ( system.startsWith( "Linux" ) )
            {
                for ( File paper : papers )
                {
                    try 
                    {
                        Runtime.getRuntime().exec( new String[] { "bash", "-c", "gs -o " + userHome + "tmp_" + paper.getName() + " -dBATCH -dNOPAUSE -dSAFER -sDEVICE=pdfwrite -sColorConversionStrategy=Gray -dProcessColorModel=/DeviceGray -f " + paper.getAbsolutePath() } );
                    }
                    catch ( IOException e )
                    {
                        JOptionPane.showMessageDialog( null, "Error in generation of " + paper.getName() + " temp file", "ERROR", JOptionPane.ERROR_MESSAGE );
                    }
                }
            }
            else if ( system.startsWith( "Windows" ) )
            {
                for ( File paper : papers )
                {
                    try 
                    {
                        System.out.println(userHome + paper.getName() + paper.getAbsolutePath());
                        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "gswin64c -sOutputFile=" + userHome + "tmp_" + paper.getName() + " -dBATCH -dNOPAUSE -dSAFER -sDEVICE=pdfwrite -sColorConversionStrategy=Gray -dProcessColorModel=/DeviceGray -f " + paper.getAbsolutePath());
                        builder.redirectErrorStream(true);
                        Process p = builder.start();
                        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        String line;
                        while (true) {
                            line = r.readLine();
                            if (line == null) { break; }
                            System.out.println(line);
                        }
                        //Process exec = Runtime.getRuntime().exec( new String[] { "cmd.exe", "/c", "gswin64c -sOutputFile=" + userHome + "tmp_" + paper.getName() + " -dBATCH -dNOPAUSE -dSAFER -sDEVICE=pdfwrite -sColorConversionStrategy=Gray -dProcessColorModel=/DeviceGray -f " + paper.getAbsolutePath() } );
                        //gswin64c -sOutputFile=C:\Users\Felipe\tmp_aceleracaopromocao.pdf -dBATCH -dNOPAUSE -dSAFER -sDEVICE=pdfwrite -sColorConversionStrategy=Gray -dProcessColorModel=/DeviceGray -f C:\Users\Felipe\OneDrive\Documentos\aceleracaopromocao.pdf                        
                    }
                    catch ( IOException e )
                    {
                        JOptionPane.showMessageDialog( null, "Error in generation of " + paper.getName() + " temp file", "ERROR", JOptionPane.ERROR_MESSAGE );
                    }
                    catch ( SecurityException e )
                    {
                        JOptionPane.showMessageDialog( null, "Error in generation of " + paper.getName() + " temp file", "ERROR", JOptionPane.ERROR_MESSAGE );
                    }
                    catch ( NullPointerException e )
                    {
                        JOptionPane.showMessageDialog( null, "Error in generation of " + paper.getName() + " temp file", "ERROR", JOptionPane.ERROR_MESSAGE );
                    }
                    catch (IndexOutOfBoundsException e )
                    {
                        JOptionPane.showMessageDialog( null, "Error in generation of " + paper.getName() + " temp file", "ERROR", JOptionPane.ERROR_MESSAGE );
                    }
                }
            }
        }
    }
    
    private void deleteTempFiles()
    {
        if ( system != null )
        {
            if ( system.startsWith( "Linux" ) )
            {
                for ( File paper : papers )
                {
                    try 
                    {
                        Runtime.getRuntime().exec( new String[] { "bash", "-c", "rm " + userHome + "tmp_" + paper.getName() } );
                    }
                    catch ( IOException e )
                    {
                        JOptionPane.showMessageDialog( null, "Error in removing " + paper.getName() + " temp file", "ERROR", JOptionPane.ERROR_MESSAGE );
                    }
                }
            }
            else if ( system.startsWith( "Windows" ) )
            {
                for ( File paper : papers )
                {
                    try 
                    {
                        Runtime.getRuntime().exec( new String[] { "cmd.exe", "/c", "del /f " + userHome.replace( "/", "\\" ) + "tmp_" + paper.getName() } );
                    }
                    catch ( IOException e )
                    {
                        JOptionPane.showMessageDialog( null, "Error in removing " + paper.getName() + " temp file", "ERROR", JOptionPane.ERROR_MESSAGE );
                    }
                }
            }
        }
    }
    
    private void extractPapers()
    {
        metapapers = new ArrayList<ArticleMeta>();
        int progress = 0;
        String filename = "";
        
        this.generateTempFiles();
        
        for ( File paper : papers )
        {
            try
            {
                filename = paper.getName();
                metapapers.add( ExtractPdf.extract( userHome, paper ) );
                progress++;
                extractProgressBar.setValue( progress * 100 / papers.length );
                extractProgressBar.setString( String.valueOf( progress * 100 / papers.length ) + "%" );
            }
            catch ( FileNotFoundException e )
            {
                JOptionPane.showMessageDialog( null, "Config file not found", "ERROR", JOptionPane.ERROR_MESSAGE );
            }
            catch ( IOException e )
            {
                JOptionPane.showMessageDialog( null, "IOException", "ERROR", JOptionPane.ERROR_MESSAGE );
            }
            catch ( AnalysisException e )
            {
                JOptionPane.showMessageDialog( null, "AnalysisException\n" + filename, "ERROR", JOptionPane.ERROR_MESSAGE );
            }
        }
        
        this.deleteTempFiles();
    }      
    
    private void saveXml()
    {
        String issueTitle = issueTittelTextField.getText();
        String issueDate = issueDateTextField.getText();
        String issueYear = issueYearTextField.getText();

        try
        {
            if ( xmlPath != null )
            {
                ExportOjs ojs = new ExportOjs( issueTitle, issueDate, xmlPath, issueYear, metapapers );
                ojs.makeXml();
                JOptionPane.showMessageDialog( null, "File created in:\n" + xmlPath, "Alert", JOptionPane.INFORMATION_MESSAGE );
            }
            else
            {
                JOptionPane.showMessageDialog( null, "You should select a file to save xml content!", "Alert", JOptionPane.WARNING_MESSAGE );
            }
        }
        catch ( FileNotFoundException e )
        {
            JOptionPane.showMessageDialog( null, "Config file not found", "ERROR", JOptionPane.ERROR_MESSAGE );
        }
        catch ( IOException e )
        {
            JOptionPane.showMessageDialog( null, "IOException", "ERROR", JOptionPane.ERROR_MESSAGE );
        }
        catch ( URISyntaxException e )
        {
            JOptionPane.showMessageDialog( null, "URISyntaxException", "ERROR", JOptionPane.ERROR_MESSAGE );
        }
    }
    
    private void startExtractionThread()
    {
        setFields( false );        
        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                extractPapers();
                setFields( true );
            }
        };
        thread.start();
    }
    
    /**private void openSectionsView()
    {
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            @Override
            public void run()
            {
                parent.setFields( false );
                final SectionView sectionView = new SectionView( parent );
                sectionView.addWindowListener( new WindowAdapter()
                {
                    @Override
                    public void windowClosing( WindowEvent e )
                    {
                        parent.setFields( true );
                        sectionView.getSections();
                    }                    
                } );
                sectionView.setVisible( true );
            }
        } );
    }**/

    private void openAddView()
    {
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            @Override
            public void run()
            {
                parent.setFields( false );
                ExtratorAddView eev = new ExtratorAddView( parent, metapapers );
                eev.addWindowListener( new WindowAdapter()
                {
                    @Override
                    public void windowClosing( WindowEvent e )
                    {
                        parent.setFields( true );
                    }
                } );
                eev.setVisible( true );
                metapapers = eev.getMetapapers();
            }
        } );
    }
    
    private void openEditView()
    {
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            @Override
            public void run()
            {
                if ( metapapers.size() > 0 )
                {
                    parent.setFields( false );
                    ExtratorEditView eev = new ExtratorEditView( parent, metapapers );
                    eev.addWindowListener( new WindowAdapter()
                    {
                        @Override
                        public void windowClosing( WindowEvent e )
                        {
                            parent.setFields( true );
                        }
                    } );
                    eev.setVisible( true );
                    metapapers = eev.getMetapapers();
                }
                else
                {
                    JOptionPane.showMessageDialog( null, "There is no metapaper to edit!", "Alert", JOptionPane.WARNING_MESSAGE );
                }
            }
        } );
    }
            
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filesLabel = new javax.swing.JLabel();
        filesTextField = new javax.swing.JTextField();
        fileChooserButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        issueTitleLabel = new javax.swing.JLabel();
        issueTittelTextField = new javax.swing.JTextField();
        issueDateLabel = new javax.swing.JLabel();
        issueDateTextField = new javax.swing.JTextField();
        issueTitleFormatLabel = new javax.swing.JLabel();
        issueYearFormatLabel = new javax.swing.JLabel();
        issueYearLabel = new javax.swing.JLabel();
        issueYearTextField = new javax.swing.JTextField();
        xmlLabel = new javax.swing.JLabel();
        xmlTextField = new javax.swing.JTextField();
        fileSaveButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        progressLabel = new javax.swing.JLabel();
        extractButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        sectionsButton = new javax.swing.JButton();
        extractProgressBar = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        filesLabel.setFont(new java.awt.Font("Lato Heavy", 0, 14)); // NOI18N
        filesLabel.setText("Files");

        filesTextField.setFont(new java.awt.Font("Lato Medium", 0, 14)); // NOI18N
        filesTextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        fileChooserButton.setFont(new java.awt.Font("Lato Heavy", 0, 14)); // NOI18N
        fileChooserButton.setText("Search");
        fileChooserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileChooserButtonActionPerformed(evt);
            }
        });

        saveButton.setFont(new java.awt.Font("Lato Heavy", 0, 14)); // NOI18N
        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        issueTitleLabel.setFont(new java.awt.Font("Lato Heavy", 0, 14)); // NOI18N
        issueTitleLabel.setText("Issue Title");

        issueTittelTextField.setFont(new java.awt.Font("Lato Medium", 0, 14)); // NOI18N
        issueTittelTextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        issueTittelTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                issueTittelTextFieldActionPerformed(evt);
            }
        });

        issueDateLabel.setFont(new java.awt.Font("Lato Heavy", 0, 14)); // NOI18N
        issueDateLabel.setText("Issue Date");

        issueDateTextField.setFont(new java.awt.Font("Lato Medium", 0, 14)); // NOI18N
        issueDateTextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        issueTitleFormatLabel.setFont(new java.awt.Font("Lato Heavy", 0, 14)); // NOI18N
        issueTitleFormatLabel.setText("YYYY-MM-DD");

        issueYearFormatLabel.setFont(new java.awt.Font("Lato Heavy", 0, 14)); // NOI18N
        issueYearFormatLabel.setText("YYYY");

        issueYearLabel.setFont(new java.awt.Font("Lato Heavy", 0, 14)); // NOI18N
        issueYearLabel.setText("Issue Year");

        issueYearTextField.setFont(new java.awt.Font("Lato Medium", 0, 14)); // NOI18N
        issueYearTextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        xmlLabel.setFont(new java.awt.Font("Lato Heavy", 0, 14)); // NOI18N
        xmlLabel.setText("XML");

        xmlTextField.setFont(new java.awt.Font("Lato Medium", 0, 14)); // NOI18N
        xmlTextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        fileSaveButton.setFont(new java.awt.Font("Lato Heavy", 0, 14)); // NOI18N
        fileSaveButton.setText("Search");
        fileSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileSaveButtonActionPerformed(evt);
            }
        });

        editButton.setFont(new java.awt.Font("Lato Heavy", 0, 14)); // NOI18N
        editButton.setText("Edit");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        progressLabel.setFont(new java.awt.Font("Lato Heavy", 0, 14)); // NOI18N
        progressLabel.setText("Progress");

        extractButton.setFont(new java.awt.Font("Lato Heavy", 0, 14)); // NOI18N
        extractButton.setText("Extract");
        extractButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                extractButtonActionPerformed(evt);
            }
        });

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        sectionsButton.setText("Sections");
        sectionsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sectionsButtonActionPerformed(evt);
            }
        });

        extractProgressBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(issueTitleLabel)
                    .addComponent(issueDateLabel)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(filesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(xmlLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(issueYearLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(progressLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(xmlTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filesTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(issueYearTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(issueDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(issueTittelTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(extractProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileSaveButton)
                    .addComponent(fileChooserButton)
                    .addComponent(issueTitleFormatLabel)
                    .addComponent(issueYearFormatLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(sectionsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(extractButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(issueTitleLabel)
                    .addComponent(issueTittelTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(issueDateLabel)
                    .addComponent(issueDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(issueTitleFormatLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(issueYearLabel)
                    .addComponent(issueYearTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(issueYearFormatLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filesTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fileChooserButton)
                    .addComponent(filesLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xmlTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fileSaveButton)
                    .addComponent(xmlLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressLabel)
                    .addComponent(extractProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton)
                    .addComponent(editButton)
                    .addComponent(extractButton)
                    .addComponent(addButton)
                    .addComponent(sectionsButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fileChooserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileChooserButtonActionPerformed
        this.openFileChooser();
    }//GEN-LAST:event_fileChooserButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        this.saveXml();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void fileSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileSaveButtonActionPerformed
        this.openFileSave();
    }//GEN-LAST:event_fileSaveButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        this.openEditView();
    }//GEN-LAST:event_editButtonActionPerformed

    private void issueTittelTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_issueTittelTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_issueTittelTextFieldActionPerformed

    private void extractButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_extractButtonActionPerformed
        this.startExtractionThread();
    }//GEN-LAST:event_extractButtonActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        this.openAddView();
    }//GEN-LAST:event_addButtonActionPerformed

    private void sectionsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sectionsButtonActionPerformed
        //this.openSectionsView();
    }//GEN-LAST:event_sectionsButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton editButton;
    private javax.swing.JButton extractButton;
    private javax.swing.JProgressBar extractProgressBar;
    private javax.swing.JButton fileChooserButton;
    private javax.swing.JButton fileSaveButton;
    private javax.swing.JLabel filesLabel;
    private javax.swing.JTextField filesTextField;
    private javax.swing.JLabel issueDateLabel;
    private javax.swing.JTextField issueDateTextField;
    private javax.swing.JLabel issueTitleFormatLabel;
    private javax.swing.JLabel issueTitleLabel;
    private javax.swing.JTextField issueTittelTextField;
    private javax.swing.JLabel issueYearFormatLabel;
    private javax.swing.JLabel issueYearLabel;
    private javax.swing.JTextField issueYearTextField;
    private javax.swing.JLabel progressLabel;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton sectionsButton;
    private javax.swing.JLabel xmlLabel;
    private javax.swing.JTextField xmlTextField;
    // End of variables declaration//GEN-END:variables
}