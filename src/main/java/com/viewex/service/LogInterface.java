package com.viewex.service;

import com.viewex.model.logModel;
import com.viewex.model.template;

public interface LogInterface {

//    template saveLog(String file_name, String user_id, String status, String content);
    logModel saveLog(logModel logmodel);
}
