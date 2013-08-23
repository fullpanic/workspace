package com.test.lwying;

import java.io.FileOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author lwying
 *
 * Aug 23, 2013
 * itext test case
 */
public class ITextTest {
    
    public static void createPDF() {
        Document document = new Document(PageSize.A4, 5, 5, 5, 5);
        try {
            PdfWriter.getInstance(document, new FileOutputStream("/software/1.pdf"));
            document.open();
            document.add(new Paragraph("helloword", FontFactory.getFont(FontFactory.COURIER,
                20,
                Font.BOLD,
                new BaseColor(255, 0, 0))));
            document.close();
        }
        catch (Exception e) {
            System.err.println("create file failed:" + e);
        }
    }
    
    /**
     * main method
     * @param args
     */
    public static void main(String[] args) {
        createPDF();
    }
}
