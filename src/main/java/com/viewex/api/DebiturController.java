package com.viewex.api;

import com.viewex.model.Debitur;
import com.viewex.service.DebiturService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/debitur")
public class DebiturController {

    @Autowired
    private DebiturService debiturApp;

    @GetMapping("")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Debitur>> getAll(){
        List<Debitur> debiturAll = debiturApp.getAllDebitur();
        return ResponseEntity.ok(debiturAll);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Debitur> showDebitur(@PathVariable int id){
        Debitur aDebiture = debiturApp.getDebitur(id);
        return ResponseEntity.ok(aDebiture);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Debitur> SelectDebitur(@PathVariable int id){
        Debitur aDebiture = debiturApp.getDebitur(id);
        return ResponseEntity.ok(aDebiture);
    }
}
