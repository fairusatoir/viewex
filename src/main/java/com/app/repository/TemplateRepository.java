package com.app.repository;

import com.app.model.Template;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends CrudRepository<Template,Integer> {

    @Query(value = "SELECT * FROM to_pdf_template t where id = :id",nativeQuery = true)
    Template findTemplate(int id);
}
