package com.henry.dao;

import com.henry.pojo.Order;
import org.apache.ibatis.annotations.Insert;

public interface OrderMapper {

    @Insert("INSERT INTO `order`(order_id, user_id, total_amount, status, created_at) " +
            "VALUES(#{orderId}, #{userId}, #{totalAmount}, #{status}, #{createdAt})")
    void insert(Order order);
}
