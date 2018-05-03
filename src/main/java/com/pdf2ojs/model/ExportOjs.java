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
package com.pdf2ojs.model;

import com.pdf2ojs.main.Main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.commons.codec.binary.Base64;
import java.util.List;
import com.pdf2ojs.model.ArticleMeta.ContributorMeta;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.PDFTextStripperByArea;

/**
 *
 * @author gustavo
 */
public class ExportOjs {

    private final String OJS_XML_HEADER = 
        "<?xml version=\"1.0\"?>\n" +
        "  <issues xmlns=\"http://pkp.sfu.ca\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://pkp.sfu.ca native.xsd\">\n" +
        "    <issue xmlns=\"http://pkp.sfu.ca\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" published=\"1\" current=\"1\" access_status=\"1\" xsi:schemaLocation=\"http://pkp.sfu.ca native.xsd\">\n";
    
    private final String OJS_ISSUE_TITLE = "${issueTitle}";
    private final String OJS_ISSUE_DATE_PUBLISHED = "${issueDatePublished}";
    private final String OJS_ISSUE_DATE_LAST_MODIFIED = "${issueDateLastModified}";
    private final String OJS_ISSUE_YEAR = "${issueyear}";
    private final String OJS_XML_ISSUE_IDENTIFICATION = 
        "      <id type=\"internal\" advice=\"ignore\">1</id>\n" +
        "        <issue_identification>\n" +
        "          <year>${issueyear}</year>\n" +
        "          <title locale=\"en_US\">${issueTitle}</title>\n" +
        "        </issue_identification>\n" +
        "        <date_published>${issueDatePublished}</date_published>\n" +
        "        <last_modified>${issueDateLastModified}</last_modified>\n";
    
    private final String OJS_XML_ISSUE_SECTIONS = 
        "      <sections>\n" +
        "        <section ref=\"ART\" seq=\"0\" editor_restricted=\"0\" meta_indexed=\"1\" meta_reviewed=\"1\" abstracts_not_required=\"0\" hide_title=\"0\" hide_author=\"0\" abstract_word_count=\"0\">\n" +
        "          <id type=\"internal\" advice=\"ignore\">1</id>\n" +
        "          <abbrev locale=\"en_US\">ART</abbrev>\n" +
        "          <title locale=\"en_US\">Articles</title>\n" +
        "        </section>\n" +
        "      </sections>\n";
    
    private final String OJS_XML_ISSUE_ARTICLES = 
        "      <articles xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://pkp.sfu.ca native.xsd\">\n";

    private final String OJS_ARTICLE_ID = "${articleID}";
    private final String OJS_ARTICLE_TITLE = "${articleTitle}";
    private final String OJS_ARTICLE_ABSTRACT = "${articleAbstract}";
    private final String OJS_ARTICLE_DATE_SUBMITTED = "${articleDateSubmitted}";
    private final String OJS_ARTICLE_DATE_PUBLISHED = "${articleDatePublished}";
    private final String OJS_XML_ISSUE_ARTICLE = 
        "        <article xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" locale=\"en_US\" date_submitted=\"${articleDateSubmitted}\" stage=\"production\" date_published=\"${articleDatePublished}\" section_ref=\"ART\" seq=\"1\" access_status=\"0\">\n" +
        "          <id type=\"internal\" advice=\"ignore\">${articleID}</id>\n" +
        "          <title locale=\"en_US\">${articleTitle}</title>\n" +
        "          <abstract locale=\"en_US\">${articleAbstract}</abstract>\n";
    
    private final String OJS_XML_ISSUE_AUTHORS = 
        "          <authors xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://pkp.sfu.ca native.xsd\">\n";
    
    private final String OJS_PRIMARY_CONTACT = "${primaryContact}";
    private final String OJS_FIRST_NAME = "${firstname}";
    private final String OJS_LAST_NAME = "${lastname}";
    private final String OJS_EMAIL = "${email}";
    private final String OJS_XML_ISSUE_AUTHOR = 
        "            <author primary_contact=\"${primaryContact}\" include_in_browse=\"true\" user_group_ref=\"Author\">\n" +
        "              <firstname>${firstname}</firstname>\n" +
        "              <lastname>${lastname}</lastname>\n" +
        "              <email>${email}</email>\n" +
        "            </author>\n";
    
    private final String OJS_XML_ISSUE_END_AUTHORS =
        "          </authors>\n";
    
    private final String OJS_FILE_NAME = "${filename}";
    private final String OJS_REV_NUMBER = "${number}";
    private final String OJS_DATE_UPLOADE = "${dateUploade}";
    private final String OJS_DATE_MODIFIED = "${dateModified}";
    private final String OJS_FILE_SIZE = "${filesize}";
    private final String OJS_AUTHOR = "${author}";
    private final String OJS_UPLOADER = "${uploader}";
    private final String OJS_EMBED = "${embed}";
    private final String OJS_SUBMISSION_ID = "${subId}";
    private final String OJS_XML_ISSUE_SUBMISSION = 
        "          <submission_file xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" stage=\"submission\" id=\"${subId}\" xsi:schemaLocation=\"http://pkp.sfu.ca native.xsd\">\n" +
        "            <revision number=\"${number}\" genre=\"Article Text\" filename=\"${filename}\" viewable=\"true\" date_uploaded=\"${dateUploade}\" date_modified=\"${dateModified}\" filesize=\"${filesize}\" filetype=\"application/pdf\" user_group_ref=\"Author\" uploader=\"${uploader}\">\n" +
        "              <name locale=\"en_US\">${author}, Author, ${filename}</name>\n" +
        "              <embed encoding=\"base64\">${embed}</embed>\n" +
        "            </revision>\n" +
        "          </submission_file>\n";
    
    private final String OJS_ARTICLE_GALLEY_ID = "${galleyId}";
    private final String OJS_ARTICLE_GALLEY_REVISION = "${revision}";
    private final String OJS_ARTICLE_REF = "${ref}";
    private final String OJS_XML_ARTICLE_GALLEY =
        "          <article_galley xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" approved=\"false\" xsi:schemaLocation=\"http://pkp.sfu.ca native.xsd\">\n" +
        "            <id type=\"internal\" advice=\"ignore\">${galleyId}</id>\n" +
        "            <name locale=\"en_US\">PDF</name>\n" +
        "            <seq>0</seq>\n" +
        "            <submission_file_ref id=\"${ref}\" revision=\"${revision}\"/>\n" +
        "          </article_galley>\n";
    
    private final String OJS_XML_ISSUE_END_ARTICLE = 
        "        </article>\n";

    private final String OJS_XML_END_FILE =
        "      </articles>\n" +
        "  </issue>\n" +
        "</issues>\n";

    
    private String articleIdName = "articleId.txt";
    //private String articleIdName = "C:Users/Nome/articleId.txt";
    private String xmlPath;
    private String issueDate;
    private String issueTitle;
    private String issueYear;
    private List<ArticleMeta> metaPapers = new ArrayList<ArticleMeta>();
    

    public ExportOjs(String issueTitle, String issueDate, String xmlPath, String issueYear, List<ArticleMeta> metaPapers) {
        this.issueTitle = issueTitle;
        this.issueDate = issueDate;
        this.issueYear = issueYear;
        this.xmlPath = xmlPath;
        this.metaPapers = metaPapers;
    }
    
    private byte[] readPdfContent(String path) throws IOException {
        String st = "";
        PDDocument document = null;
        document = PDDocument.load(new File(path));
        document.getClass();
        if (!document.isEncrypted()) {
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(true);
            PDFTextStripper Tstripper = new PDFTextStripper();
            st = Tstripper.getText(document);
        }
        return st.getBytes();
    }
    
    private int getArticleId() throws IOException, URISyntaxException {
        String fileName = articleIdName;
        File jarFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        String inputFilePath = jarFile.getParent() + File.separator + fileName;         
        return Integer.parseInt(this.fileToString(inputFilePath));
    }
    
    private void saveArticleId(int id) throws IOException, URISyntaxException {
        String fileName = articleIdName;
        File jarFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        String inputFilePath = jarFile.getParent() + File.separator + fileName;
        Files.write(Paths.get(inputFilePath), String.valueOf(id).getBytes(), StandardOpenOption.WRITE);
    }
    
    private String fileToString(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        StringBuilder builder = new StringBuilder();
        String line;    

        // For every line in the file, append it to the string builder
        System.out.println("aqui");
        while((line = reader.readLine()) != null) {
            builder.append(line);
        }

        reader.close();
        return builder.toString();
}
   
    private String getAuthorName(ContributorMeta author){
        String s[] = author.getName().split(" ");
        return author.getName().split(" ")[s.length-1];
    }
    
    private String getAuthorEmail(ContributorMeta author)
    {
        return (author.getEmail() == null) ? "noemail@domain.com" : author.getEmail();
    }
    
    public void makeXml() throws FileNotFoundException, IOException, URISyntaxException {
        String resultXml = OJS_XML_HEADER;
        Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(xmlPath), "utf-8"));
        
        String strIssue;
        strIssue = OJS_XML_ISSUE_IDENTIFICATION;
        
        
        strIssue = strIssue.replace(OJS_ISSUE_TITLE, this.issueTitle);
        strIssue = strIssue.replace(OJS_ISSUE_DATE_PUBLISHED, this.issueDate);
        strIssue = strIssue.replace(OJS_ISSUE_DATE_LAST_MODIFIED, this.issueDate);
        strIssue = strIssue.replace(OJS_ISSUE_YEAR, this.issueYear);

        resultXml += strIssue + OJS_XML_ISSUE_SECTIONS + OJS_XML_ISSUE_ARTICLES;
        writer.write(resultXml);
        resultXml = "";
        
        int articleId = this.getArticleId();
        int nUnknownAuthor = 0;
        for (ArticleMeta am : this.metaPapers){
            String strArticle = OJS_XML_ISSUE_ARTICLE;
            String strSubmission = OJS_XML_ISSUE_SUBMISSION;
            String strGalley = OJS_XML_ARTICLE_GALLEY;
            String strAuthor;
            String strAuthors = "";            
            articleId++;
            boolean firstAuthor = true;

            if ((am.getTitle().isEmpty()) || (am.getTitle().equals(""))) {
                continue;
            }
            
            strArticle = strArticle.replace(OJS_ARTICLE_ID, String.valueOf(articleId));
            strArticle = strArticle.replace(OJS_ARTICLE_DATE_SUBMITTED, this.issueDate);
            strArticle = strArticle.replace(OJS_ARTICLE_DATE_PUBLISHED, this.issueDate);
            strArticle = strArticle.replace(OJS_ARTICLE_TITLE, am.getTitle());
            strArticle = strArticle.replace(OJS_ARTICLE_ABSTRACT, am.getAbstractText());
               
            strSubmission = strSubmission.replace(OJS_FILE_NAME, "article" + am.getPdf().getName());
            strSubmission = strSubmission.replace(OJS_REV_NUMBER, String.valueOf(articleId));
            strSubmission = strSubmission.replace(OJS_DATE_UPLOADE, this.issueDate);
            strSubmission = strSubmission.replace(OJS_DATE_MODIFIED, this.issueDate);
            strSubmission = strSubmission.replace(OJS_FILE_SIZE, String.valueOf(am.getPdf().length()));
            strSubmission = strSubmission.replace(OJS_EMBED, Base64.encodeBase64String(Files.readAllBytes(Paths.get(am.getPdf().getAbsolutePath()))));
            strSubmission = strSubmission.replace(OJS_SUBMISSION_ID, String.valueOf(articleId));

            strGalley = strGalley.replace(OJS_ARTICLE_GALLEY_ID, String.valueOf(articleId));
            strGalley = strGalley.replace(OJS_ARTICLE_GALLEY_REVISION, String.valueOf(articleId));
            strGalley = strGalley.replace(OJS_ARTICLE_REF, String.valueOf(articleId));

            if (am.getAuthors().size() < 1) {
                ContributorMeta unknownAuthor = new ContributorMeta();
                unknownAuthor.setName("Unknown Author");
                am.getAuthors().add(unknownAuthor);
                
                System.out.println("Paper: " + am.getTitle());
                System.out.println("Unknown Author");
                nUnknownAuthor++;
            }
            
            for (ContributorMeta author : am.getAuthors()) {
                if (author.getName().length() > 40) {
                    System.out.println("Continue");
                    continue;
                }
                strAuthor = OJS_XML_ISSUE_AUTHOR;
                if (author.getName().isEmpty()) {
                    strAuthor = strAuthor.replace(OJS_FIRST_NAME, "Author");
                } else {
                    strAuthor = strAuthor.replace(OJS_FIRST_NAME, author.getName().substring(0, author.getName().lastIndexOf(" ")));
                }
                strAuthor = strAuthor.replace(OJS_LAST_NAME, this.getAuthorName(author));
                strAuthor = strAuthor.replace(OJS_EMAIL, "noemail@domain.com"/*this.getAuthorEmail(author)*/);
                strAuthor = strAuthor.replace(OJS_PRIMARY_CONTACT, String.valueOf(firstAuthor));
                               
                if (firstAuthor) {
                    strSubmission = strSubmission.replace(OJS_AUTHOR, this.getAuthorName(author));
                    strSubmission = strSubmission.replace(OJS_UPLOADER, "ojs_sbc");

                    firstAuthor = false;
                }
                strAuthors += strAuthor;
            }
            
            strAuthors = OJS_XML_ISSUE_AUTHORS + strAuthors + OJS_XML_ISSUE_END_AUTHORS;
            resultXml +=  strArticle + strAuthors + strSubmission + strGalley + OJS_XML_ISSUE_END_ARTICLE;
            writer.write(resultXml);
            resultXml = "";
        }
        resultXml += OJS_XML_END_FILE;
        this.saveArticleId(articleId);
        writer.write(resultXml);
        writer.close();
        
        System.out.println("Finished xml process: " + nUnknownAuthor + " authors unknown");
    }

}
