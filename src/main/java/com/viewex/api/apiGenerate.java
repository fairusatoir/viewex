package com.viewex.api;

import com.lowagie.text.DocumentException;
import com.viewex.model.template;
import com.viewex.service.pdfGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;

@RestController
//@Controller
@RequestMapping("/generate-file")
public class apiGenerate {

    @Autowired
    private pdfGenerator pdfService;

    @CrossOrigin
    @GetMapping("/trial")
    public String StringTrial(){
        return "Berhasil Jalan API nya";
    }

//    @CrossOrigin
    @GetMapping("")
    public String configPDF(
            @RequestParam("name") String name,
            @RequestParam("idTemplate") int idTemplate,
            @RequestParam("fileType") String fileType,
            @RequestParam("content") String content,
            @RequestParam("userId") String userId,
            Model model
    ) throws IOException, DocumentException {
        template templateData = pdfService.getTemplate(idTemplate);

        String[] contents = content.split("\\*");

        if (contents.length != templateData.getContent_sum()){
            model.addAttribute("Error","Jumlah Tidak sama");
            model.addAttribute("params",contents.length);
            model.addAttribute("contentDB",templateData.getContent_sum());
            return "ErrorPage";
//            return contents;
        }else {
            ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setSuffix(".html");
            templateResolver.setTemplateMode(TemplateMode.HTML);

            TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(templateResolver);

            Context context = new Context();
//            context.setVariable("to", "Baeldung");

            String[] contentDB = templateData.getFormat_content().split(",");

            for (int i = 0; i < contents.length; i++){
                if (contents[i].equals("")){
                    model.addAttribute("Error","Ada yg Null Boss");
//                    return "ErrorPage";

                }else {
//                    String[] value = contents[i].split(":");
//                    model.addAttribute(value[0],value[1]);
                    context.setVariable(contentDB[i],contents[i]);
                }
            }
//            context.setVariable("nomorAplikasi","hati");

//            String file = templateData.getTemplate_file();
            String htmltopdfCOndif = templateEngine.process("index", context);
//            return contents;

            String outputFolder = System.getProperty("user.home") + File.separator + "thymeleaf.pdf";
            OutputStream outputStream = new FileOutputStream(outputFolder);

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmltopdfCOndif);
            renderer.layout();
            renderer.createPDF(outputStream);

            outputStream.close();
        }
        return "OKEE";
    }
}
