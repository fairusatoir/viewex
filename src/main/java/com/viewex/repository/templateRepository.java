package com.viewex.repository;

import com.viewex.model.template;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface templateRepository extends CrudRepository<template,Integer> {

    @Query(value = "SELECT * FROM test.dbo.template t where id = :id",nativeQuery = true)
    template findTempalte(int id);
}
