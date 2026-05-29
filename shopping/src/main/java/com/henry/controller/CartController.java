package com.henry.controller;

import com.henry.cache.GuestCache;
import com.henry.pojo.*;
import com.henry.service.CartItemService;
import com.henry.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private GuestCache guestCache;

    private String getOrGenerateCartId(HttpSession session) {
        // 1. 優先檢查是否為「已登入會員」
        Object userId = session.getAttribute("userId");
        if (userId != null) {
            // 已登入，回傳會員專屬識別碼
            return "USER_" + userId.toString();
        }
        // 2. 若未登入，檢查 Session 中是否已經有「訪客的購物車 ID」
        String guestCartId = (String) session.getAttribute("guestCartId");
        if (guestCartId == null) {
            // 3. 若為初次點擊加入購物車的訪客，生成一組 UUID
            guestCartId = UUID.randomUUID().toString();
            // 4. 將這組 UUID 存入 Session 中，命名為 "guestCartId" 而不是 "userId"
            session.setAttribute("guestCartId", guestCartId);
        }
        // 回傳訪客專屬識別碼
        return "GUEST_" + guestCartId;
    }

    private Integer getCartId(HttpSession session) {
        String cartIdentifier = getOrGenerateCartId(session);
        Integer cartId = null;
        if (cartIdentifier.startsWith("USER_")) {
            cartId = (Integer) session.getAttribute("cartId");
        } else {
            String guestUuid = cartIdentifier.replace("GUEST_", "");
            cartId = guestCache.getGuestCartId(guestUuid);
        }
        return cartId;
    }

    /*
     * 1.選擇之商品加入購物車
     * 2.跳轉到購物車詳細頁面
     */
    @PostMapping("/items")
    public String addItemToCart(
            CartItemRequestDTO request,
            HttpSession session){

        // 1. 取得classfierId
        String cartIdentifier = getOrGenerateCartId(session);

        Integer cartId = null;

        // 2. 判斷是訪客還是會員
        if (cartIdentifier.startsWith("USER_")) {
            cartId = (Integer) session.getAttribute("cartId");
        } else {
            // 3 確認購物車是否存在
            String guestUuid = cartIdentifier.replace("GUEST_","");
            cartId = guestCache.getGuestCartId(guestUuid);
            // 2.1 購物車不存在，新增並存入DB
            if (cartId == null) {
                // userId因為是訪客所以保持為null
                Cart cart = new Cart();
                cart.setCreatedAt(LocalDateTime.now());
                cart.setUpdatedAt(LocalDateTime.now());
                int num = cartService.addCart(cart);
                cartId = cart.getCartId();
                guestCache.setGuestIdMap(guestUuid, cartId);
            }
        }

        cartItemService.addItemToCart(request, cartId);

        return "redirect:/products";
    }

    @GetMapping("/detail")
    public String toCartDetail(HttpSession session, ModelMap modelMap){

        // 1.取的使用者的cartId
        String cartIdentifier = getOrGenerateCartId(session);
        Integer cartId = null;
        if (cartIdentifier.startsWith("USER_")) {
            cartId = (Integer) session.getAttribute("cartId");
        } else {
            String guestUuid = cartIdentifier.replace("GUEST_","");
            cartId = guestCache.getGuestCartId(guestUuid);
        }

        // 2.cartId取出對應cartItemDTO(price, productId, quantity, stockQuantity)
        CartItemDetailDTO dto = new CartItemDetailDTO();
        List<CartItemDetailDTOItem> items = cartItemService.getCartItemsByCartId(cartId);
        dto.setItems(items);

        //2.1 計算totalAmount
        Integer totalAmount = 0;
        totalAmount +=
                items.stream().mapToInt(item -> item.getPrice() * item.getQuantity()).sum();
        dto.setTotalAmount(totalAmount);

        // 3.items放入request
        modelMap.addAttribute("cart", dto);

        // 4.跳轉cartDetail.html
        return "cartDetail";
    }

    // 快速購買，新增商品到購物車後，跳轉頁面到cartDetail.html
    @PostMapping("/quick-buy")
    public String quickBuy(
            CartItemRequestDTO request,
            HttpSession session){

        // 1. 取得classfierId
        String cartIdentifier = getOrGenerateCartId(session);

        Integer cartId = null;

        // 2. 判斷是訪客還是會員
        if (cartIdentifier.startsWith("USER_")) {
            cartId = (Integer) session.getAttribute("cartId");
        } else {
            // 3 確認購物車是否存在
            String guestUuid = cartIdentifier.replace("GUEST_","");
            cartId = guestCache.getGuestCartId(guestUuid);
            // 2.1 購物車不存在，新增並存入DB
            if (cartId == null) {
                // userId因為是訪客所以保持為null
                Cart cart = new Cart();
                cart.setCreatedAt(LocalDateTime.now());
                cart.setUpdatedAt(LocalDateTime.now());
                int num = cartService.addCart(cart);
                cartId = cart.getCartId();
                guestCache.setGuestIdMap(guestUuid, cartId);
            }
        }

        // 取得cartId

        cartItemService.addItemToCart(request, cartId);

        return "redirect:/cart/detail";
    }

    @PostMapping("/update")
    public String updateCartDetailItem(
        HttpSession session,
        @RequestParam("productId") Integer productId,
        @RequestParam("quantity") Integer quantity){

        // 1.取得cartId
        Integer cartId = getCartId(session);

        // 2.取得cartItem
        CartItem cartItem = cartItemService.getByCartIdAndProductId(cartId, productId);

        // 3.更新數量(注意庫存)
        cartItem.setQuantity(quantity);

        // 4.更新DB
        cartItemService.modifyCartItem(cartItem);

        // 5.返回邏輯視圖名稱
        return "redirect:/cart/detail";
    }

    @DeleteMapping("/items/{productId}")
    public String removeCartItemByProductId(
            HttpSession session,
            @PathVariable Integer productId){
        Integer cartId = getCartId(session);
        cartItemService.deleteByCartIdAndProductId(cartId, productId);
        return "redirect:/cart/detail";
    }
}
