package com.app.service;

import com.lowagie.text.DocumentException;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class PdfMakerService {

    public void GeneratePdfFromHtml(String fileName,String htmltopdfCOndif) throws IOException, DocumentException {
        String outputFolder = "pdf/" + fileName;
        OutputStream outputStream = new FileOutputStream(outputFolder);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmltopdfCOndif);
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
    }
}
