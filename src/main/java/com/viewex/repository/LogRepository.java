package com.viewex.repository;

import com.viewex.model.logModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends CrudRepository<logModel, Integer> {

}
