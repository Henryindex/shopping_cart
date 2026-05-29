package com.henry.service;

import com.henry.dao.CartItemMapper;
import com.henry.dao.CartMapper;
import com.henry.pojo.Cart;
import com.henry.pojo.CartItem;
import com.henry.pojo.CartItemRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    private CartMapper cartMapper;

    public int addCart(Cart cart) {
        return cartMapper.insertCart(cart);
    }

    // 將訪客的購物車資訊寫入會員的購物車當中
    public void mergeCartItems(Integer guestCartId, Integer cartId) {
        // 1.取得cartId為guest或user之商品
        List<CartItem> guestItems = cartItemMapper.selectByCartId(guestCartId);
        List<CartItem> userItems = cartItemMapper.selectByCartId(cartId);
        if (guestItems.isEmpty()) {
            return;
        }

        Map<Integer, CartItem> userItemMap =
                userItems.stream().collect(Collectors.toMap(CartItem::getProductId, item -> item));

        for (CartItem cartItem : guestItems) {

            Integer productId = cartItem.getProductId();

            if (userItemMap.containsKey(productId)) {
                // 2.1.productId相同，數量相加並存入user中
                CartItem existCartItem = userItemMap.get(productId);
                existCartItem.setQuantity(existCartItem.getQuantity() + cartItem.getQuantity());
                cartItemMapper.updateCartItem(existCartItem);
            } else {
                // 2.2.productId不同，新增一筆user之商品
                CartItem newItem = new CartItem();
                newItem.setProductId(cartItem.getProductId());
                newItem.setQuantity(cartItem.getQuantity());
                newItem.setAddedPrice(cartItem.getAddedPrice());
                newItem.setCartId(cartId);
                cartItemMapper.insertCartItem(newItem);
            }
        }

        // 3.刪除guestCartId的商品
        cartItemMapper.deleteByCartId(guestCartId);
    }
}
