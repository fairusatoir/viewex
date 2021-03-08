package com.viewex.repository;

import com.viewex.model.Template;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TemplateRepository extends CrudRepository<Template,Integer> {

    @Query(value = "SELECT * FROM to_pdf_template t where id = :id",nativeQuery = true)
    Template findTemplate(int id);
}
