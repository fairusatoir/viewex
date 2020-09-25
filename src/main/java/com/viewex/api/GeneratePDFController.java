package com.viewex.api;

import com.viewex.model.logModel;
import com.viewex.repository.LogRepository;
import com.viewex.repository.TemplateRepository;
import org.springframework.core.io.ByteArrayResource;
import com.lowagie.text.DocumentException;
import com.viewex.Exception.Error;
import com.viewex.model.template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/generate-file")
public class GeneratePDFController {

    @Autowired
    private TemplateRepository templateRepo;

    @Autowired
    private LogRepository logRepo;

    @CrossOrigin
    @GetMapping("/trial")
    public String StringTrial(){
        return "Berhasil Jalan API nya";
    }

    @CrossOrigin
    @GetMapping(value = "", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> configPDF(
            @RequestParam("name") String name,
            @RequestParam("idTemplate") int idTemplate,
            @RequestParam("fileType") String fileType,
            @RequestParam("content") String content,
            @RequestParam("userId") String userId
    ) throws DocumentException {

        template templateDataDB = templateRepo.findTempalte(idTemplate);
        String[] contents = content.split("\\*");
        String fileName = name+"."+fileType;

        try {

            if (templateDataDB == null) { //Check Template Document is exist
                SaveFailedToPDF(name,userId,content);
                throw new Error("Template not Found");

            } else {

                if (contents.length != templateDataDB.getContent_sum()) { //check the amount of content
                    SaveFailedToPDF(name,userId,content);
                    throw new Error("The mount of Content does not match, check Content");
                } else {

                    GeneratePDFController thymeleaf2Pdf = new GeneratePDFController();
                    String htmlConfig = thymeleaf2Pdf.ParseThymeleafTemplate(templateDataDB, contents);
                    thymeleaf2Pdf.GeneratePdfFromHtml(fileName,htmlConfig);

                    ResponseEntity<Resource> httpPDF = DownloadFile(fileName, name, userId,content);

                    LocalDate localDate = LocalDate.now();

                    logRepo.save(
                            new logModel(name,
                                    Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                                    userId,
                                    "SUCCESS",
                                    content));

                    return httpPDF;
                }
            }
        }catch (IOException e){
            SaveFailedToPDF(name,userId,content);
            throw new Error("Error! try again", e);
        }
    }

    public ResponseEntity<Resource> DownloadFile(
            String fileName,
            String name,
            String  userId,
            String  content)
            throws IOException {

        File file = new File("src/main/resources/pdf/" + fileName);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+ fileName);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        try {
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

            return ResponseEntity.ok()
                    .headers(header)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);

        }catch (IOException e){
            SaveFailedToPDF(name,userId,content);
            throw new Error("Failed to download file "+fileName+" | "+ e.toString());
        }
    }

    private String ParseThymeleafTemplate(
            template templateDataDB,
            String[] contents){

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        String[] contentDB = templateDataDB.getFormat_content().split(",");

        Context context = ReplaceContent(contents,contentDB);

        return templateEngine.process(templateDataDB.getTemplate_file(), context);
    }

    private void GeneratePdfFromHtml(String fileName,String htmltopdfCOndif) throws IOException, DocumentException{
        String outputFolder = "src/main/resources/pdf/" + fileName;
        OutputStream outputStream = new FileOutputStream(outputFolder);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmltopdfCOndif);
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();
    }

    private Context ReplaceContent(String[] contents, String[] contentDB){
        Context context = new Context();
        Boolean notError = true;

        for (int i = 0; i < contents.length; i++) {

            if (contents[i].equals("")) { //check the content is not null
                notError = false;
                throw new Error("There is a null content section");
            } else {

                if (!contents[i].contains("_")){ //Check content with "_"

                    context.setVariable(contentDB[i], contents[i]);

                }else {
                    if ( IsEmailValid(contents[i]) ){ //  Check is email ?
                        context.setVariable(contentDB[i], contents[i]);
                    }else {

                        String[] nameArray = contents[i].split("_");
                        String nameUser = "";
                        for (String separateName : nameArray){
                            nameUser= nameUser+ separateName +" ";
                        }
                        context.setVariable(contentDB[i], nameUser);
                    }
                }
            }
        }
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm");
        String formattedDate = myDateObj.format(myFormatObj);

        if (notError){
            context.setVariable(
                    "dateNow",
                    formattedDate);
        }
        return context;
    }

    private boolean IsEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    private void SaveFailedToPDF(String name,String userId,String content){

        LocalDate localDate = LocalDate.now();

        logRepo.save(
            new logModel(name,
                    Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    userId,
                    "FAILED",
                    content));
    }
}


