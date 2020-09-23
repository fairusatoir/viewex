package com.viewex.service;

import com.viewex.exception.FileStorageException;
import com.viewex.exception.MyFileNotFoundException;
import com.viewex.model.template;
import com.viewex.property.FileStorageProperties;
import com.viewex.repository.templateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class pdfGeneratorMethod implements pdfGenerator {

//    private final Path fileStorageLocation;

//    @Autowired
//    public pdfGeneratorMethod(FileStorageProperties fileStorageProperties) {
//        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
//                .toAbsolutePath().normalize();
//
//        try {
//            Files.createDirectories(this.fileStorageLocation);
//        } catch (Exception ex) {
//            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
//        }
//    }

    @Autowired
    private templateRepository templateDB;

    @Override
    public template getTemplate(int id) {
        return templateDB.findTempalte(id);
    }

    @Override
    public Resource loadFileAsResouce(String fileName) {
        try {
//            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Path filePath = Paths.get("pdf/");
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
}
