package com.henry.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    // 檢查是否有登入
    // 檢查是否為訪客登入
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // session.userId檢查是否登入
        Object userId = request.getSession().getAttribute("userId");
        Object guestCartId = request.getSession().getAttribute("guestCartId");
        // 如果非會員且非訪客，前往登入頁面
        if (userId == null && guestCartId == null) {
            response.sendRedirect("/login/login");
            return false;
        }
        return true;
    }
}
