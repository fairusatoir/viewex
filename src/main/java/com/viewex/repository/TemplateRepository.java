package com.viewex.repository;

import com.viewex.model.template;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TemplateRepository extends CrudRepository<template,Integer> {

    @Query(value = "SELECT * FROM to_pdf_template t where id = :id",nativeQuery = true)
    template findTempalte(int id);
}
