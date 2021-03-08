package com.app.service;

import com.app.common.Desc;
import com.app.model.Template;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ValidationService {

    private Desc desc;

    public String checkParamsNotNull(
            String name,
            String templateDataDB,
            String fileType,
            String content,
            String userId
    ){
        try {
            if(name.isEmpty() || name.equals("")){
                return desc.paramsNameNull;
            }else if(fileType.isEmpty() || name.equals("") ){
                return desc.paramsFileTypeNull;
            }else if(templateDataDB.isEmpty() || templateDataDB.equals("")){
                return desc.paramsIdTemplateNull;
            }else if(content.isEmpty() || content.equals("") ){
                return desc.paramsContentNull;
            }else if(userId.isEmpty() || userId.equals("") ){
                return desc.paramsUserIdNull;
            }else{
                return "";
            }
        }catch (Exception e){
            System.out.println(e.toString());
            return desc.somthingIsWrong;
        }
    }

    public String checkRequiredContent(String fileType, String[] contents, Template templateDataDB){
        try {
            List<String> types = Arrays.asList(templateDataDB.getFile_type().split("\\*"));

            if (contents.length != templateDataDB.getContent_sum()) { // Check the amount of content
                return desc.paramsContentSize;
            }else if(!types.contains(fileType)){ // File Type Available
                return desc.paramsFileTypeNotFound;
            }else if(!isNotNullContent(contents)){ // File Type Available
                return desc.paramsContentFillNull;
            }else{
                return "";
            }
        }catch (Exception e){
            System.out.println(e.toString());
            return desc.somthingIsWrong;
        }
    }

    public boolean isNotNullContent(String[] contents){
        boolean isNotNull = true;
        for (int i = 0; i < contents.length; i++) {
            if (contents[i].equals("")) { //check the content is not null
                isNotNull = false;
            }
        }
        return isNotNull;
    }

    public boolean IsEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

}
