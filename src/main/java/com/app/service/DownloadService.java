package com.app.service;

import com.app.controller.ErrorHandling;
import com.app.common.Desc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class DownloadService {

    @Autowired
    private LogService logService;

    private Desc desc;
    private ErrorHandling error;

    public ResponseEntity<Resource> file(
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

            logService.SaveSuccess(name,userId,content);

            return ResponseEntity.ok()
                    .headers(header)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);

        }catch (IOException e){
            logService.SaveFailed(name,userId,content,desc.failedDownload);
            return error.respone(desc.failedDownload);
        }
    }
}
