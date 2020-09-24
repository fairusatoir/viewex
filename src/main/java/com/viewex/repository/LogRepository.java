package com.viewex.repository;

import com.viewex.model.logModel;
import com.viewex.model.template;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends CrudRepository<logModel, Integer> {

//    @Query(value = "INSERT INTO test.dbo.log_pdf VALUES (:file_name, GETDATE(), :user_id , :status , :content  )",nativeQuery = true)
//    template saveLog(String file_name, String user_id, String status, String content);
}
