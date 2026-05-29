package com.henry.dao;

import com.henry.pojo.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapper {

    // 使用商品ID查詢商品詳情
    @Select("SELECT * FROM product WHERE id=#{id}")
    public Product selectById(Integer id);

    @Select("SELECT * FROM product")
    public List<Product> selectAll();

    List<Product> selectByIds(List<Integer> ids);
}
