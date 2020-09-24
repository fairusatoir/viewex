package com.viewex.service.method;

import com.viewex.model.logModel;
import com.viewex.repository.LogRepository;
import com.viewex.service.LogInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogMethod implements LogInterface {

    @Autowired
    private LogRepository logDB;

    @Override
    public logModel saveLog(logModel logmodel) {
        return logDB.save(logmodel);
    }
}
