package com.viewex.api;

import com.viewex.model.Product;
import com.viewex.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Controller
@RequestMapping("/product")
public class productController {

    @Autowired
    private ProductService productService;

    @GetMapping("")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Product>> getAll(){
        List<Product> productsAll = productService.getAll();
        return ResponseEntity.ok(productsAll);
    }

    @GetMapping("/page")
    public String showProducts(){
        return "test";
    }
}
