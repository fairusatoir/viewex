package com.app.service;

import com.app.model.Template;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
public class HtmlMakerService {

    @Autowired
    private ValidationService check;

    String namabulan[] = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};

    public String ParseThymeleafTemplate(
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

                if(parameterHtml[i].contains("day") || parameterHtml[i].contains("date")){
                    contents[i] = dateFormating(contents[i]);
                }

                context.setVariable(parameterHtml[i], contents[i]);

            }else {
                if ( check.IsEmail(contents[i]) ){ //  Check is email
                    context.setVariable(parameterHtml[i], contents[i]);
                }else {

                    String[] nameArray = contents[i].split("_"); // john_smith => john smith
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
        context.setVariable("dateNow",dateFormating(myDateObj.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        return context;
    }

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

    private String dateFormating(String strDate){
        strDate = strDate.replaceAll("-","/");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(strDate,formatter);
        String dateFix = date.getDayOfMonth()+" "+this.namabulan[date.getMonthValue()-1]+" "+date.getYear();
        return dateFix;
    }



}
