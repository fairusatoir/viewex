package com.viewex.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class Error implements ErrorController {

    public final static String paramsNameNull = "Name Document is Null!";
    public final static String paramsIdTemplateNull = "Id Template Document is Null!";
    public final static String paramsIdTemplateNotFound = "Id Template Document Not Found!";
    public final static String paramsContentNull = "Content is Null!";
    public final static String paramsContentSize = "The amount of content is not the same as the template!";
    public final static String paramsContentFillNull = "There is null data in the params content!";
    public final static String paramsUserIdNull = "userId is Null!";
    public final static String paramsFileTypeNull = "FileType Document is Null!";
    public final static String paramsFileTypeNotFound = "FileType Document Not Found!";
    public final static String failedDownload = "Failed Download!";
    public final static String somthingIsWrong = "Service Error, Try again!";

    @CrossOrigin
    @RequestMapping("/error")
    public String handleError(HttpServletResponse httpResponse) throws Exception {
        httpResponse.sendRedirect("/generate-file");
        return null;
    }

    @Override
    public String getErrorPath() {
        return null;
    }


}
