package com.viewex.service;

import com.viewex.model.template;
import com.viewex.repository.templateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class pdfGeneratorMethod implements pdfGenerator {

    @Autowired
    private templateRepository templateDB;

    @Override
    public template getTemplate(int id) {
        return templateDB.findTempalte(id);
    }
}
