package com.viewex.controller;

import com.viewex.model.LogGenerate;
import com.viewex.repository.LogRepository;
import com.viewex.repository.TemplateRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import com.lowagie.text.DocumentException;
import com.viewex.model.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

@RestController
public class GeneratePDFController {

    @Autowired
    private TemplateRepository templateRepo;

    @Autowired
    private LogRepository logRepo;

    private final static Logger LOGGER = null;
    private String descError;

    @CrossOrigin
    @GetMapping("/")
    public ModelAndView index(){
        ModelAndView index = new ModelAndView("indexDoc");
        return index;
    }

    @CrossOrigin
    @GetMapping("/test")
    public String StringTrial(){
        return "Berhasil Jalan API nya";
    }

    @CrossOrigin
    @GetMapping(value = "print", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> configPDF(
            @RequestParam("name") String name,
            @RequestParam("idTemplate") String idTemplate,
            @RequestParam("fileType") String fileType,
            @RequestParam("content") String content,
            @RequestParam("userId") String userId
    ) throws DocumentException {
        try {
            descError = checkParamsNotNull(name,idTemplate,fileType,content,userId);
            if(!descError.isEmpty()){ // Check Params Null
                SaveFailedToPDF(name,userId,content,descError);
                return ErrorRespone(descError);
            }else{
                Template templateDataDB = templateRepo.findTemplate(Integer.parseInt(idTemplate));
                if (templateDataDB == null ){ // Check Template in Database
                    SaveFailedToPDF(name,userId,content,Error.paramsIdTemplateNotFound);
                    return ErrorRespone(Error.paramsIdTemplateNotFound);
                }else{
                    String[] contents = content.split("\\*");
                    descError = checkRequiredContent(fileType, contents, templateDataDB);
                    if(!descError.isEmpty()){ // Check if Content and Format valid
                        SaveFailedToPDF(name,userId,content,descError);
                        return ErrorRespone(descError);
                    }else{
                        String fileName = name+"."+fileType;
                        GeneratePDFController thymeleaf2Pdf = new GeneratePDFController();
                        String htmlConfig = thymeleaf2Pdf.ParseThymeleafTemplate(templateDataDB, contents);
                        thymeleaf2Pdf.GeneratePdfFromHtml(fileName,htmlConfig);

                        ResponseEntity<Resource> httpPDF = DownloadFile(fileName, name, userId,content);
                        return httpPDF;
                    }
                }
            }
        }catch (IOException e){
            SaveFailedToPDF(name,userId,content,Error.somthingIsWrong);
            return ErrorRespone(Error.somthingIsWrong);

        }
    }

    private String checkParamsNotNull(
            String name,
            String templateDataDB,
            String fileType,
            String content,
            String userId
    ){
        try {
            if(name.isEmpty() || name.equals("")){
                return Error.paramsNameNull;
            }else if(fileType.isEmpty() || name.equals("") ){
                return Error.paramsFileTypeNull;
            }else if(templateDataDB.isEmpty() || templateDataDB.equals("")){
                return Error.paramsIdTemplateNull;
            }else if(content.isEmpty() || content.equals("") ){
                return Error.paramsContentNull;
            }else if(userId.isEmpty() || userId.equals("") ){
                return Error.paramsUserIdNull;
            }else{
                return "";
            }
        }catch (Exception e){
            System.out.println(e.toString());
            return Error.somthingIsWrong;
        }
    }

    private String checkRequiredContent(String fileType, String[] contents, Template templateDataDB){
        try {
            List<String> types = Arrays.asList(templateDataDB.getFile_type().split("\\*"));

            if (contents.length != templateDataDB.getContent_sum()) { // Check the amount of content
                return Error.paramsContentSize;
            }else if(!types.contains(fileType)){ // File Type Available
                return Error.paramsFileTypeNotFound;
            }else if(!isNotNullContent(contents)){ // File Type Available
                return Error.paramsContentFillNull;
            }else{
                return "";
            }
        }catch (Exception e){
            System.out.println(e.toString());
            return Error.somthingIsWrong;
        }
    }

    private boolean isNotNullContent(String[] contents){
        boolean isNotNull = true;
        for (int i = 0; i < contents.length; i++) {
            if (contents[i].equals("")) { //check the content is not null
                isNotNull = false;
            }
        }
        return isNotNull;
    }

    private String ParseThymeleafTemplate(
            Template templateDataDB,
            String[] contents
    ){

        FileTemplateResolver templateResolver = new FileTemplateResolver();
        templateResolver.setPrefix("templates\\\\");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        String[] parameterHtml = templateDataDB.getFormat_content().split(",");

        Context context = ReplaceContent(contents,parameterHtml, templateDataDB.getImage());

        return templateEngine.process(templateDataDB.getTemplate_file(), context);
    }

    private Context ReplaceContent(
            String[] contents,
            String[] parameterHtml,
            String imageFileName
    ){
        Context context = new Context();

        for (int i = 0; i < contents.length; i++) {
            if (!contents[i].contains("_")){ //Check content with "_"

                context.setVariable(parameterHtml[i], contents[i]);

            }else {
                if ( IsEmailValid(contents[i]) ){ //  Check is email ?
                    context.setVariable(parameterHtml[i], contents[i]);
                }else {

                    String[] nameArray = contents[i].split("_");
                    String nameUser = "";
                    for (String separateName : nameArray){
                        nameUser= nameUser+ separateName +" ";
                    }
                    context.setVariable(parameterHtml[i], nameUser);
                }
            }

        }

        if(imageFileName != null){
            Path path = Paths.get("templates/image/"+imageFileName);
            String base64Image = convertToBase64(path);

            String image = "data:image/png;base64," + base64Image;
            context.setVariable("watermark", image);
        }

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm");
        String formattedDate = myDateObj.format(myFormatObj);
        context.setVariable("dateNow",formattedDate);

        return context;
    }

//    private String dateFormat()

    private String convertToBase64(Path path) {
        byte[] imageAsBytes = new byte[0];
        try {
            Resource resource = new UrlResource(path.toUri());
            InputStream inputStream = resource.getInputStream();
            imageAsBytes = IOUtils.toByteArray(inputStream);

        } catch (IOException e) {
            System.out.println("\n File read Exception");
        }
        return Base64.getEncoder().encodeToString(imageAsBytes);
    }

    private boolean IsEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    private void GeneratePdfFromHtml(String fileName,String htmltopdfCOndif) throws IOException, DocumentException{
        String outputFolder = "pdf/" + fileName;
        OutputStream outputStream = new FileOutputStream(outputFolder);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmltopdfCOndif);
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
    }

    private void SaveFailedToPDF(String name,String userId,String content, String description){

        logRepo.save(
            new LogGenerate(name,
                    LocalDateTime.now(),
                    userId,
                    "FAILED",
                    content,
                    description
                    ));
    }

    private ResponseEntity<Resource> ErrorRespone(String ErrorDesc){
        ByteArrayResource ErrorResource = new ByteArrayResource(ErrorDesc.getBytes());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResource);
    }



    private ResponseEntity<Resource> DownloadFile(
            String fileName,
            String name,
            String  userId,
            String  content
    )throws IOException {

        File file = new File("pdf/" + fileName);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+ fileName);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        try {
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

            LocalDate localDate = LocalDate.now();

            logRepo.save(
                    new LogGenerate(name,
                            LocalDateTime.now(),
                            userId,
                            "SUCCESS",
                            content));

            return ResponseEntity.ok()
                    .headers(header)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);

        }catch (IOException e){
            SaveFailedToPDF(name,userId,content,Error.failedDownload);
            return ErrorRespone(Error.failedDownload);
        }
    }

}