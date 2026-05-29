package com.henry.controller;

import com.henry.pojo.Product;
import com.henry.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    // 轉往id=xxx之商品詳情頁面
    @GetMapping("/products/{id}")
    public String toProductDetail(@PathVariable Integer id, ModelMap modelMap){
        Product product = productService.findById(id);
        modelMap.addAttribute("product", product);
        return "productDetail";
    }

    // 轉往所有商品頁面
    @GetMapping("/products")
    public String toProductList(ModelMap modelMap){
        List<Product> products = productService.getAll();
        modelMap.addAttribute("products", products);
        return "productList";
    }
}
