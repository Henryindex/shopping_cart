package com.henry.dao;

import com.henry.pojo.OrderItem;

import java.util.List;

public interface OrderItemMapper {

    int batchInsert(List<OrderItem> items);
}
