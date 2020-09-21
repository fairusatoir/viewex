package com.viewex.service;

import com.viewex.model.Debitur;

import java.util.List;
public interface DebiturInterfaceService {

    List<Debitur> getAllDebitur();

    Debitur getDebitur(int id);
}
