package com.app.repository;

import com.app.model.LogGenerate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends CrudRepository<LogGenerate, Integer> {

}
