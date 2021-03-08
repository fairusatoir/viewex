package com.app.service;

import com.app.model.LogGenerate;
import com.app.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepo;

    public void SaveSuccess(String name,String userId,String content){
        logRepo.save(
                new LogGenerate(name,
                        LocalDateTime.now(),
                        userId,
                        "SUCCESS",
                        content));
    }

    public void SaveFailed(String name,String userId,String content, String description){

        logRepo.save(
                new LogGenerate(name,
                        LocalDateTime.now(),
                        userId,
                        "FAILED",
                        content,
                        description
                ));
    }

}
