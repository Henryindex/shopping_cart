package com.henry.controller;

import com.henry.pojo.CartItem;
import com.henry.pojo.Order;
import com.henry.pojo.OrderRespDTO;
import com.henry.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/checkpoint")
    public String checkpoint(HttpSession session, ModelMap modelMap) {
        Integer userId = (Integer) session.getAttribute("userId");
        OrderRespDTO respDTO = orderService.saveOrder(userId);
        modelMap.addAttribute("order", respDTO);
        return "checkout-success";
    }
}
