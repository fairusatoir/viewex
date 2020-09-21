package com.viewex.repository;

import com.viewex.model.Debitur;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DebiturRepository extends CrudRepository<Debitur,Integer> {

    @Query(value = "SELECT * FROM test.dbo.test",nativeQuery = true)
    List<Debitur> findAllDebitur();

    @Query(value = "SELECT * FROM test.dbo.test WHERE id_ktp = :id",nativeQuery = true)
    Debitur findDebitur(int id);
}
