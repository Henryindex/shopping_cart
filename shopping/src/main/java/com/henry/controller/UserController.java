package com.henry.controller;

import com.henry.pojo.User;
import com.henry.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String toRegisterPage() {
        return "register";
    }

    // 返回更新會員基本資訊頁面
    @GetMapping("/profile")
    public String toProfile(HttpSession session, ModelMap modelMap){
        Integer userId = (Integer) session.getAttribute("userId");
        User user = userService.getUserById(userId);
        modelMap.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/register")
    public String processRegister(User user, ModelMap modelMap) {
        String respMsg = userService.addNewUser(user);
        if (respMsg != null) {
            modelMap.addAttribute("error", respMsg);
            return "register";
        } else {
            return "redirect:/login/login?registered=true";
        }
    }

    @PutMapping("/profile")
    public String modifyUser(HttpSession session, User user){
        // 1.取得原本的User
        Integer userId = (Integer) session.getAttribute("userId");
        // 2.更新User信息
        int num = userService.modifyUserInfo(userId, user);
        // 3.寫回DB
        return "redirect:/";
    }


}
