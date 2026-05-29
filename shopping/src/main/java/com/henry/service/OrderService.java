package com.henry.service;

import com.henry.dao.*;
import com.henry.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    CartItemMapper cartItemMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Transactional(rollbackFor = Exception.class)
    public OrderRespDTO saveOrder(Integer userId) {

        OrderRespDTO resp = new OrderRespDTO();

        // Get user by id
        User user = userMapper.selectById(userId);

        // create Order id(為了orderId)
        Integer totalAmount = 0;
        String orderId = UUID.randomUUID().toString();

        // 提取cartId=XXX的cartItem
        Integer cartId = user.getCartId();
        List<CartItem> cartItems = cartItemMapper.selectByCartId(cartId);
        List<Integer> productIds = cartItems.stream().map(cartItem -> cartItem.getProductId()).distinct().toList();
        List<Product> products = productMapper.selectByIds(productIds);
        Map<Integer, Product> productMap =
                products.stream().collect(
                        Collectors.toMap(product -> product.getId(), product -> product)
                );

        // for-loop處理cartItem -> orderItem
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {

            // Create OrderItem and set values
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());

            // Name and price must be current
            Product product = productMapper.selectById(cartItem.getProductId());
            orderItem.setProductName(product.getName());
            orderItem.setPurchasePrice(product.getPrice());

            // Add to List
            orderItems.add(orderItem);

            // Calculate total amount
            totalAmount += product.getPrice()*cartItem.getQuantity();

        }

        // Batch insert orderitem list
        orderItemMapper.batchInsert(orderItems);

        // Create and Insert Order todatabase
        LocalDateTime now = LocalDateTime.now();
        Order order = new Order();
        order.setOrderId(orderId);
        order.setStatus(Status.PENDING);
        order.setTotalAmount(totalAmount);
        order.setUserId(userId);
        order.setCreatedAt(now);

        // Insert Order into DB
        orderMapper.insert(order);

        // prepare resp dto
        resp.setOrderId(orderId);
        resp.setTotalAmount(totalAmount);
        resp.setItems(orderItems);

        return resp;
    }
}
