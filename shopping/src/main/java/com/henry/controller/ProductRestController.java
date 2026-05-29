package com.henry.controller;

import com.henry.pojo.Product;
import com.henry.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {

    @Autowired
    ProductService productService;

    // 簡單回傳id=XXX的商品資訊
    @GetMapping("/api/products/{id}")
    public Product getProductInfoById(@PathVariable Integer id) {
        Product product = productService.findById(id);
        return product;
    }
}
