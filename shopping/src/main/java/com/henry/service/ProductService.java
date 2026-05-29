package com.henry.service;

import com.henry.dao.ProductMapper;
import com.henry.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    public Product findById(Integer id){
        Product product = productMapper.selectById(id);
        System.out.println(product);
        if (product != null)
            return product;
        else
            return null;
    }

    public List<Product> getAll(){
        List<Product> products = productMapper.selectAll();
        return products;
    }
}
