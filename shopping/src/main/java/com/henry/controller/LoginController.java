package com.henry.controller;

import com.henry.cache.GuestCache;
import com.henry.pojo.Cart;
import com.henry.pojo.User;
import com.henry.service.CartService;
import com.henry.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

import java.util.UUID;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private GuestCache guestCache;

    @GetMapping("/login")
    public String toLoginPage(){
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session,
            ModelMap modelMap){
        int num = 0;
        // 執行登入邏輯
        // 1.根據資料庫搜尋user
        User user = userService.getUser(username, password);
        // 2.如果有該user，確認是否有對應的cartId
        if (user != null) {
            Integer userId = user.getId();
            Integer cartId = user.getCartId();
            // 3.如果沒有cartId，生成一個新的cart，寫入DB並將ID寫入user
            if (cartId == null)  {
                Cart cart = new Cart();
                cart.setUserId(userId);
                cart.setCreatedAt(LocalDateTime.now());
                cart.setUpdatedAt(LocalDateTime.now());
                num = cartService.addCart(cart);
                cartId = cart.getCartId();
                user.setCartId(cartId);
                num = userService.modifyUser(user);
            }

            String guestUuid = (String) session.getAttribute("guestCartId");
            if (guestUuid != null) {
                Integer guestCartId = guestCache.getGuestCartId(guestUuid);
                if (guestCartId != null) {
                    // 呼叫 CartService 處理：將臨時購物車 (guestCartId) 的明細轉移到正式會員購物車 (cartId)
                    cartService.mergeCartItems(guestCartId, cartId);
                    // 轉移成功後，記得從記憶體快取中移除，避免 OOM 記憶體溢出
                    guestCache.removeCartId(guestUuid);
                }
            }

            session.setAttribute("userId", userId);
            session.setAttribute("cartId", cartId);
            session.setAttribute("username", username);
            return "redirect:/";
        } else {
            // 轉回Login Page，使用訪客登入
            modelMap.addAttribute("errorMessage", "帳號或密碼錯誤");
            return "login";
        }
    }

    @PostMapping("/guest")
    public String guestIn(HttpSession session) {
        // 1. 檢查目前 Session 裡是否已經有訪客 UUID
        String guestUuid = (String) session.getAttribute("guestCartId");
        if (guestUuid == null) {
            // 2. 如果是全新訪客，當場生成 UUID 並存入 Session 域
            guestUuid = UUID.randomUUID().toString();
            session.setAttribute("guestCartId", guestUuid);
        }
        // 安全防範：確保排除會員標籤
        session.removeAttribute("userId");
        session.removeAttribute("cartId");
        return "redirect:/products";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        // 清除session內的數據
        session.removeAttribute("userId");
        session.removeAttribute("cartId");
        session.removeAttribute("guestCartId");
        session.removeAttribute("username");
        return  "redirect:/";
    }
}
