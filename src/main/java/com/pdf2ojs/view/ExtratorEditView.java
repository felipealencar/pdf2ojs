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

import com.pdf2ojs.model.ArticleMeta.ContributorMeta;
import com.pdf2ojs.model.ArticleMeta;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class ExtratorEditView extends javax.swing.JFrame {

    private List<ArticleMeta> metapapers = new ArrayList<ArticleMeta>();
    private List<JTextField> authorNames = new ArrayList<JTextField>();
    private int index = 0;
    private int max = 0;
    private ExtractorView parent;
    
    /**
     *
     * @param parent
     * @param metaPapers
     */
    public ExtratorEditView(ExtractorView parent, List<ArticleMeta> metaPapers) {
        initComponents();
        initAuthorNames();
        this.metapapers = metaPapers;
        this.parent = parent;
        max = this.metapapers.size();
        abstractTextArea.setLineWrap(true);
        abstractTextArea.setWrapStyleWord(true);
        abstractTextArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        setFields();
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    public List<ArticleMeta> getMetapapers(){
        return metapapers;
    }
    
    private void nextPaper(){
        index++;
        System.err.println("index: " + index);
        if (index > max-1){
            index = 0;
            System.err.println("Ultrapassou o MAX volta para: " + index);
        }
        setFields();
    }
    
    private void backPaper() {
        index--;
        System.err.println("index: " + index);
        if (index < 0){
            index = max-1;
            System.err.println("Ultrapassou o MIN volta para: " + index);
        }
        setFields();
    }
    
    private void setFields() {
        int fieldIndex = 0;
        ArticleMeta article = metapapers.get(index);
        titleTextField.setText(article.getTitle());
        abstractTextArea.setText(article.getAbstractText());
        
        for (ContributorMeta author: article.getAuthors()) {
            authorNames.get(fieldIndex).setText(author.getName());
            if (author.getName().length() > 40){
                authorNames.get(fieldIndex).setBackground(Color.red);
            } else {
                authorNames.get(fieldIndex).setBackground(Color.WHITE);
            }
            fieldIndex++;
        }
        fileLabel.setText(article.getPdf().getName());
    }
    
    private void saveFields(int index) {
        List<ContributorMeta> authors = new ArrayList<ContributorMeta>();
        int authorIndex = 0;
        metapapers.get(index).setTitle(titleTextField.getText());
        metapapers.get(index).setAbstractText(abstractTextArea.getText());

        for (JTextField authorName : authorNames) {
            if (!authorName.getText().isEmpty()) {
                ContributorMeta cm = new ContributorMeta();
                cm.setName(authorName.getText());
                authors.add(cm);
                
                System.out.println("Paper: " + metapapers.get(index).getTitle());
                System.out.println("Saving authors:\n");
                System.out.println("- " + cm.getName());
            }
        }

        metapapers.get(index).setAuthors(authors);
    }
    
private void initAuthorNames() {
        authorNames.add(author1TextField);
        authorNames.add(author2TextField);
        authorNames.add(author3TextField);
        authorNames.add(author4TextField);
        authorNames.add(author5TextField);
        authorNames.add(author6TextField);
        authorNames.add(author7TextField);
        authorNames.add(author8TextField);
        authorNames.add(author9TextField);
        authorNames.add(author10TextField);
    }
    
    private void cleanFields()
    {
        abstractTextArea.setText("");
        titleTextField.setText("");
        for (JTextField jtf: authorNames){
            jtf.setText("");
        }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TitleLabel = new javax.swing.JLabel();
        titleTextField = new javax.swing.JTextField();
        author1TextField = new javax.swing.JTextField();
        author1Label = new javax.swing.JLabel();
        author2TextField = new javax.swing.JTextField();
        author2Label = new javax.swing.JLabel();
        author4Label = new javax.swing.JLabel();
        author3TextField = new javax.swing.JTextField();
        autho4Label = new javax.swing.JLabel();
        author4TextField = new javax.swing.JTextField();
        author5Label = new javax.swing.JLabel();
        author5TextField = new javax.swing.JTextField();
        author6Label = new javax.swing.JLabel();
        author6TextField = new javax.swing.JTextField();
        author7TextField = new javax.swing.JTextField();
        author7Label = new javax.swing.JLabel();
        author8TextField = new javax.swing.JTextField();
        author8Label = new javax.swing.JLabel();
        author9TextField = new javax.swing.JTextField();
        author9Label = new javax.swing.JLabel();
        author10TextField = new javax.swing.JTextField();
        author10Label = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        abstractTextArea = new javax.swing.JTextArea();
        backButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        fileLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        TitleLabel.setFont(new java.awt.Font("Lato Black", 0, 14)); // NOI18N
        TitleLabel.setText("Paper Title");

        titleTextField.setFont(new java.awt.Font("Lato Medium", 0, 14)); // NOI18N
        titleTextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        titleTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                titleTextFieldActionPerformed(evt);
            }
        });

        author1TextField.setFont(new java.awt.Font("Lato Medium", 0, 14)); // NOI18N
        author1TextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        author1Label.setFont(new java.awt.Font("Lato Black", 0, 14)); // NOI18N
        author1Label.setText("Author 1");

        author2TextField.setFont(new java.awt.Font("Lato Medium", 0, 14)); // NOI18N
        author2TextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        author2Label.setFont(new java.awt.Font("Lato Black", 0, 14)); // NOI18N
        author2Label.setText("Author 2");

        author4Label.setFont(new java.awt.Font("Lato Black", 0, 14)); // NOI18N
        author4Label.setText("Author 3");

        author3TextField.setFont(new java.awt.Font("Lato Medium", 0, 14)); // NOI18N
        author3TextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        autho4Label.setFont(new java.awt.Font("Lato Black", 0, 14)); // NOI18N
        autho4Label.setText("Author 4");

        author4TextField.setFont(new java.awt.Font("Lato Medium", 0, 14)); // NOI18N
        author4TextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        author5Label.setFont(new java.awt.Font("Lato Black", 0, 14)); // NOI18N
        author5Label.setText("Author 5");

        author5TextField.setFont(new java.awt.Font("Lato Medium", 0, 14)); // NOI18N
        author5TextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        author6Label.setFont(new java.awt.Font("Lato Black", 0, 14)); // NOI18N
        author6Label.setText("Author 6");

        author6TextField.setFont(new java.awt.Font("Lato Medium", 0, 14)); // NOI18N
        author6TextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        author7TextField.setFont(new java.awt.Font("Lato Medium", 0, 14)); // NOI18N
        author7TextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        author7Label.setFont(new java.awt.Font("Lato Black", 0, 14)); // NOI18N
        author7Label.setText("Author 7");

        author8TextField.setFont(new java.awt.Font("Lato Medium", 0, 14)); // NOI18N
        author8TextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        author8Label.setFont(new java.awt.Font("Lato Black", 0, 14)); // NOI18N
        author8Label.setText("Author 8");

        author9TextField.setFont(new java.awt.Font("Lato Medium", 0, 14)); // NOI18N
        author9TextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        author9Label.setFont(new java.awt.Font("Lato Black", 0, 14)); // NOI18N
        author9Label.setText("Author 9");

        author10TextField.setFont(new java.awt.Font("Lato Medium", 0, 14)); // NOI18N
        author10TextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        author10Label.setFont(new java.awt.Font("Lato Black", 0, 14)); // NOI18N
        author10Label.setText("Author 10");

        abstractTextArea.setColumns(20);
        abstractTextArea.setFont(new java.awt.Font("Lato Medium", 0, 14)); // NOI18N
        abstractTextArea.setRows(20);
        abstractTextArea.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jScrollPane1.setViewportView(abstractTextArea);

        backButton.setFont(new java.awt.Font("Lato Black", 0, 14)); // NOI18N
        backButton.setText("<<");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        saveButton.setFont(new java.awt.Font("Lato Black", 0, 14)); // NOI18N
        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        nextButton.setFont(new java.awt.Font("Lato Black", 0, 14)); // NOI18N
        nextButton.setText(">>");
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        fileLabel.setFont(new java.awt.Font("Lato Black", 0, 14)); // NOI18N
        fileLabel.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 940, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(author9Label)
                                    .addComponent(author8Label)
                                    .addComponent(author7Label)
                                    .addComponent(author6Label)
                                    .addComponent(author5Label)
                                    .addComponent(autho4Label)
                                    .addComponent(author4Label)
                                    .addComponent(author2Label)
                                    .addComponent(author1Label)
                                    .addComponent(TitleLabel))
                                .addComponent(author10Label, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(author1TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 858, Short.MAX_VALUE)
                                .addComponent(author2TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 858, Short.MAX_VALUE)
                                .addComponent(author3TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 858, Short.MAX_VALUE)
                                .addComponent(author4TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 858, Short.MAX_VALUE)
                                .addComponent(author5TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 858, Short.MAX_VALUE)
                                .addComponent(author6TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 858, Short.MAX_VALUE)
                                .addComponent(author7TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 858, Short.MAX_VALUE)
                                .addComponent(author8TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 858, Short.MAX_VALUE)
                                .addComponent(author9TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 858, Short.MAX_VALUE)
                                .addComponent(author10TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 858, Short.MAX_VALUE)
                                .addComponent(titleTextField)))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(backButton)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(saveButton)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(nextButton)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(fileLabel))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TitleLabel)
                    .addComponent(titleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(author1Label)
                    .addComponent(author1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(author2Label)
                    .addComponent(author2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(author4Label)
                    .addComponent(author3TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(autho4Label)
                    .addComponent(author4TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(author5Label)
                    .addComponent(author5TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(author6Label)
                    .addComponent(author6TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(author7Label)
                    .addComponent(author7TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(author8Label)
                    .addComponent(author8TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(author9Label)
                    .addComponent(author9TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(author10TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(author10Label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backButton)
                    .addComponent(saveButton)
                    .addComponent(nextButton)
                    .addComponent(fileLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void titleTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_titleTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_titleTextFieldActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        cleanFields();
        nextPaper();
    }//GEN-LAST:event_nextButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        cleanFields();
        backPaper();
    }//GEN-LAST:event_backButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        saveFields(index);
    }//GEN-LAST:event_saveButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel TitleLabel;
    private javax.swing.JTextArea abstractTextArea;
    private javax.swing.JLabel autho4Label;
    private javax.swing.JLabel author10Label;
    private javax.swing.JTextField author10TextField;
    private javax.swing.JLabel author1Label;
    private javax.swing.JTextField author1TextField;
    private javax.swing.JLabel author2Label;
    private javax.swing.JTextField author2TextField;
    private javax.swing.JTextField author3TextField;
    private javax.swing.JLabel author4Label;
    private javax.swing.JTextField author4TextField;
    private javax.swing.JLabel author5Label;
    private javax.swing.JTextField author5TextField;
    private javax.swing.JLabel author6Label;
    private javax.swing.JTextField author6TextField;
    private javax.swing.JLabel author7Label;
    private javax.swing.JTextField author7TextField;
    private javax.swing.JLabel author8Label;
    private javax.swing.JTextField author8TextField;
    private javax.swing.JLabel author9Label;
    private javax.swing.JTextField author9TextField;
    private javax.swing.JButton backButton;
    private javax.swing.JLabel fileLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextField titleTextField;
    // End of variables declaration//GEN-END:variables
}
