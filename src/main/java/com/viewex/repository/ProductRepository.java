package com.viewex.repository;

import com.viewex.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query(value = "SELECT * FROM test.dbo.product",nativeQuery = true)
    List<Product> findAllProduct();
}