package com.viewex.api;

import com.lowagie.text.DocumentException;
import com.viewex.model.template;
import com.viewex.service.pdfGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

//@RestController
@Controller
@RequestMapping("/generate-file")
public class apiGenerate {

    private static final Logger logger = LoggerFactory.getLogger(apiGenerate.class);

    @Autowired
    private pdfGenerator pdfService;

    @CrossOrigin
    @GetMapping("/trial")
    public String StringTrial(){
        return "Berhasil Jalan API nya";
    }

//    @CrossOrigin
    @GetMapping("")
    public ResponseEntity<Resource> configPDF(
//    public String configPDF(
            @RequestParam("name") String name,
            @RequestParam("idTemplate") int idTemplate,
            @RequestParam("fileType") String fileType,
            @RequestParam("content") String content,
            @RequestParam("userId") String userId,
            Model model,
            HttpServletRequest request
    ) throws IOException, DocumentException {
        template templateData = pdfService.getTemplate(idTemplate);

        String[] contents = content.split("\\*");
        String outputFolder = "pdf/" +name +"."+fileType;

        if (contents.length != templateData.getContent_sum()){
            model.addAttribute("Error","Jumlah Tidak sama");
            model.addAttribute("params",contents.length);
            model.addAttribute("contentDB",templateData.getContent_sum());
//            return "ErrorPage";
            logger.info("Could not determine file type.");
        }else {
            ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setPrefix("templates/");
            templateResolver.setSuffix(".html");
            templateResolver.setTemplateMode(TemplateMode.HTML);

            TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(templateResolver);

            Context context = new Context();

            String[] contentDB = templateData.getFormat_content().split(",");

            for (int i = 0; i < contents.length; i++){
                if (contents[i].equals("")){
                    model.addAttribute("Error","Ada yg Null Boss");
//                    return "ErrorPage";
                    logger.info("Could not determine file type.");

                }else {
                    model.addAttribute(contentDB[i],contents[i]);
                    context.setVariable(contentDB[i],contents[i]);
                }
            }
            
//            Generate PDF
            String nameFilehtml = templateData.getTemplate_file();
            String htmltopdfCOndif = templateEngine.process(nameFilehtml, context);

//            String outputFolder = "pdf/" +name +"."+fileType;
            OutputStream outputStream = new FileOutputStream(outputFolder);

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmltopdfCOndif);
            renderer.layout();
            renderer.createPDF(outputStream);

            outputStream.close();

//            return nameFilehtml;
        }
//        Download pdf
        Resource resource = pdfService.loadFileAsResouce(name);
        String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        var body = ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + outputFolder + "\"")
                .body(resource);
        return body;
    }
}
