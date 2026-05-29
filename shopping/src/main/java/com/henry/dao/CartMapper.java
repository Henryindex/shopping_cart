package com.henry.dao;

import com.henry.pojo.Cart;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CartMapper {

    @Select("SELECT * FROM cart_id WHERE id=#{cartId}")
    public Cart getById(Integer cartId);

    @Insert("INSERT INTO cart(user_id, created_at, updated_at) VALUES(#{userId}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "cartId")
    public int insertCart(Cart cart);
}
