package com.app.controller;

import com.app.common.Desc;
import com.app.repository.TemplateRepository;
import com.app.service.*;
import com.lowagie.text.DocumentException;
import com.app.model.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

@RestController
public class FileMakerController {

    private final static Logger LOGGER = null;

    @Autowired
    private TemplateRepository templateRepo;
    @Autowired
    private LogService logService;
    @Autowired
    private HtmlMakerService thyme;
    @Autowired
    private PdfMakerService flying;
    @Autowired
    private ValidationService check;
    @Autowired
    private DownloadService download;

    private Desc desc;
    private ErrorHandling error;

    private String descError;

    @CrossOrigin
    @GetMapping("/")
    public ModelAndView index(){
        ModelAndView index = new ModelAndView("indexDoc");
        return index;
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
            this.descError = check.checkParamsNotNull(name,idTemplate,fileType,content,userId);
            if(!this.descError.isEmpty()){ // Check Params Null
                logService.SaveFailed(name,userId,content,this.descError);
                return error.respone(this.descError);
            }else{
                Template templateDataDB = templateRepo.findTemplate(Integer.parseInt(idTemplate));
                if (templateDataDB == null ){ // Check Template in Database
                    logService.SaveFailed(name,userId,content,desc.paramsIdTemplateNotFound);
                    return error.respone(desc.paramsIdTemplateNotFound);
                }else{
                    String[] contents = content.split("\\*");
                    this.descError = check.checkRequiredContent(fileType, contents, templateDataDB);
                    if(!this.descError.isEmpty()){ // Check if Content and Format valid
                        logService.SaveFailed(name,userId,content,this.descError);
                        return error.respone(this.descError);
                    }else{
                        String fileName = name+"."+fileType;
                        String htmlConfig = thyme.ParseThymeleafTemplate(templateDataDB, contents);
                        flying.GeneratePdfFromHtml(fileName,htmlConfig);

                        ResponseEntity<Resource> httpPDF = download.file(fileName, name, userId,content);
                        return httpPDF;
                    }
                }
            }
        }catch (IOException e){
            logService.SaveFailed(name,userId,content,desc.somthingIsWrong);
            return error.respone(desc.somthingIsWrong);

        }
    }

}