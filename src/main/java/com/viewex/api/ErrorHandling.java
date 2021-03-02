package com.viewex.api;

import org.springframework.boot.web.servlet.error.ErrorController;
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
}
