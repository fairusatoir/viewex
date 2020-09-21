package com.viewex.service;

import com.viewex.model.Debitur;
import com.viewex.repository.DebiturRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebiturService implements DebiturInterfaceService{

    @Autowired
    private DebiturRepository DebiturDB;

    @Override
    public List<Debitur> getAllDebitur() {
        return DebiturDB.findAllDebitur();
    }

    @Override
    public Debitur getDebitur(int id) {
        return DebiturDB.findDebitur(id);
    }


}
