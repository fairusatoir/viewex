package com.app.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class ErrorHandling implements ErrorController {

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

    public ResponseEntity<Resource> respone(String ErrorDesc){
        ByteArrayResource ErrorResource = new ByteArrayResource(ErrorDesc.getBytes());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResource);
    }

}
