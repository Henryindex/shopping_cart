package com.henry.service;

import com.henry.dao.CartItemMapper;
import com.henry.pojo.CartItem;
import com.henry.pojo.CartItemDetailDTOItem;
import com.henry.pojo.CartItemRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {

    @Autowired
    CartItemMapper cartItemMapper;

    public void addItemToCart(CartItemRequestDTO request, Integer cartId) {
        // 1. 根據productId與cartId取出cartItem
        Integer productId = request.getProductId();
        CartItem cartItem = cartItemMapper.selectByCartIdAndProductId(cartId, productId);
        // 2. 判斷cartItem是否為null
        if (cartItem == null) {
            // 2.1 cartItem==null，新增該商品到購物車
            cartItem = new CartItem();
            cartItem.setCartId(cartId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setAddedPrice(request.getPrice());
            int num = cartItemMapper.insertCartItem(cartItem);
        } else {
            // 2.2 cartItem!=null，更改商品數量並更新DB
            Integer addQuantity = request.getQuantity();
            Integer quantity = cartItem.getQuantity();
            // 判斷庫存，暫時先跳過
            cartItem.setQuantity(addQuantity+quantity);
            int num = cartItemMapper.updateCartItem(cartItem);
        }
    }

    public List<CartItemDetailDTOItem> getCartItemsByCartId(Integer cartId) {
        return cartItemMapper.selectCartItemDetailDTOsByCartId(cartId);
    }

    public CartItem getByCartIdAndProductId(Integer cartId, Integer productId) {
        return cartItemMapper.selectByCartIdAndProductId(cartId, productId);
    }

    public void modifyCartItem(CartItem cartItem) {
        cartItemMapper.updateCartItem(cartItem);
    }

    public void deleteByCartIdAndProductId(Integer cartId, Integer productId) {
        cartItemMapper.deleteByCartIdAndProductId(cartId, productId);
    }
}
