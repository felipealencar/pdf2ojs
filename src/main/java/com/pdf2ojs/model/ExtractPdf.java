/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sbc.sbceventpdfextractor.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFText2HTML;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.PDFTextStripperByArea;
import org.jdom.Element;
import org.jdom.Document;
import pl.edu.icm.cermine.ContentExtractor;
import pl.edu.icm.cermine.exception.AnalysisException;


/**
 *
 * @author gustavo
 */
public class ExtractPdf {
    

    static private String replaceString(String abs) {
        String result = abs;
        result = result.replaceAll("c¸", "ç");
        result = result.replaceAll("ç ", "ç");
        result = result.replaceAll("a˜", "ã");
        result = result.replaceAll("a´", "á");
        result = result.replaceAll("aˆ", "â");
        result = result.replaceAll("a`", "á");
        result = result.replaceAll("e´", "é");
        result = result.replaceAll("eˆ", "ê");
        result = result.replaceAll("´ı", "í");
        result = result.replaceAll("o´", "ó");
        result = result.replaceAll("o˜", "õ");
        result = result.replaceAll("oˆ", "õ");
        result = result.replaceAll("u´", "ú");
        
        result = result.replaceAll("A˜", "Ã");
        result = result.replaceAll("A´", "Á");
        result = result.replaceAll("Aˆ", "Â");
        result = result.replaceAll("A`", "Á");
        result = result.replaceAll("E´", "É");
        result = result.replaceAll("Eˆ", "Ê");
        result = result.replaceAll("´I", "Í");
        result = result.replaceAll("O´", "Ó");
        result = result.replaceAll("O˜", "õ");
        result = result.replaceAll("Oˆ", "Ô");
        result = result.replaceAll("U´", "Ú");
        
        result = result.replaceAll("ã o ", "ão ");
        result = result.replaceAll(" á", "á");
        result = result.replace(" ú", "ú");
        
        return result;
    }
    
    static private String getSbcAbstract(String sbcAbstract) {
        String resumo = sbcAbstract;
        
        if (sbcAbstract.contains("Resumo.")){
            String split[] = sbcAbstract.split("Resumo. ");
            resumo = split[1];
        }
        
        System.out.println(resumo);
        return resumo;
    }
    
    static public ArticleMeta extract(File paper) throws AnalysisException, FileNotFoundException, IOException  {
        /* Get Elements from the pdf */
        ContentExtractor extractor;
        extractor = new ContentExtractor();
        List<ArticleMeta.ContributorMeta> index = new ArrayList<ArticleMeta.ContributorMeta>();
        
        InputStream inputStream = new FileInputStream(paper.getAbsolutePath());              
        extractor.setPDF(inputStream);
        Element result = extractor.getContentAsNLM();
        Document doc = new Document(result);

        /* Parse to xml to manipulate */
        ArticleMeta articleMeta = ArticleMeta.extractNLM(doc);
        articleMeta.setPdf(paper);
        articleMeta.setAbstractText(getSbcAbstract(articleMeta.getAbstractText()));
        articleMeta.setAbstractText(replaceString(articleMeta.getAbstractText()));
        articleMeta.setTitle(replaceString(articleMeta.getTitle()));
        
        for (ArticleMeta.ContributorMeta author : articleMeta.getAuthors()) {
            author.setName(replaceString(author.getName()));
            if (author.getName().contains("@")) {
                System.out.println("remover " + articleMeta.getAuthors().indexOf(author));
                index.add(author);
            }
        }
        
        System.out.println("Size index: " + index.size());

        for (ArticleMeta.ContributorMeta i : index){
            System.out.println("index: " + i);
            articleMeta.getAuthors().remove(i);
        }
        
        

        return articleMeta;
    }
    
}
