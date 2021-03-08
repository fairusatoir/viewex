package com.viewex.repository;

import com.viewex.model.LogGenerate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends CrudRepository<LogGenerate, Integer> {

}
