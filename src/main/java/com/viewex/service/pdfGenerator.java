package com.viewex.service;

import com.viewex.model.template;
import org.springframework.core.io.Resource;

public interface pdfGenerator {

     template getTemplate(int id);
}
