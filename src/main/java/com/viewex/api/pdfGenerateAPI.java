package com.viewex.api;

import com.lowagie.text.DocumentException;
import com.viewex.Exception.Error;
import com.viewex.model.template;
import com.viewex.service.pdfGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

//@RestController
@Controller
@RequestMapping("/generate-file")
public class pdfGenerateAPI {

    @Autowired
    private pdfGenerator pdfService;

    @CrossOrigin
    @GetMapping("/trial")
    public String StringTrial(){
        return "Berhasil Jalan API nya";
    }

    @GetMapping("")
    public String configPDF(
            @RequestParam("name") String name,
            @RequestParam("idTemplate") int idTemplate,
            @RequestParam("fileType") String fileType,
            @RequestParam("content") String content,
            @RequestParam("userId") String userId,
            Model model
    ) throws IOException, DocumentException {
        try {
            template templateData = pdfService.getTemplate(idTemplate);

            if (templateData == null) {
                throw new Error("Template not Found");
            } else {

                String[] contents = content.split("\\*");

                if (contents.length != templateData.getContent_sum()) {
                    throw new Error("the amount of content does not match");
                } else {

                    pdfGenerateAPI thymeleaf2Pdf = new pdfGenerateAPI();
                    String html = thymeleaf2Pdf.parseThymeleafTemplate(templateData, contents, model);
                    thymeleaf2Pdf.generatePdfFromHtml(name,fileType,html);

                    return templateData.getTemplate_file();
                }
            }
        }catch (IOException e){
            throw new Error("Error! try again", e);
        }
    }

    public void generatePdfFromHtml(String name, String fileType,String htmltopdfCOndif) throws IOException, DocumentException{
        String outputFolder = "pdf/" + name + "." + fileType;
        OutputStream outputStream = new FileOutputStream(outputFolder);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmltopdfCOndif);
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();
    }

    private String parseThymeleafTemplate(template templateData, String[] contents, Model model){
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();

        String[] contentDB = templateData.getFormat_content().split(",");

        for (int i = 0; i < contents.length; i++) {
            if (contents[i].equals("")) {
                throw new Error("There is a null content section");
            } else {
                model.addAttribute(contentDB[i], contents[i]);
                context.setVariable(contentDB[i], contents[i]);
            }
        }
        String nameFileTemplateHTML = templateData.getTemplate_file();

        return templateEngine.process(nameFileTemplateHTML, context);
    }

}


