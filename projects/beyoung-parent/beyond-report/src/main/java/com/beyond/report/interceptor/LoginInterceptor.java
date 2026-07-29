package com.beyond.report.interceptor;

import com.beyond.report.model.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 檢查 Session 是否已登入：
 * - 一般頁面請求（demo1 等）未登入 -> 302 導回登入頁
 * - AJAX / API 請求未登入 -> 回傳 401，交由前端跳轉
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        boolean isLogin = session != null && session.getAttribute(LoginUser.SESSION_KEY) != null;

        if (isLogin) {
            return true;
        }

        String requestedWith = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(requestedWith);

        if (isAjax) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"Success\":\"N\",\"LoginMsg\":\"尚未登入或登入逾時，請重新登入\"}");
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
        return false;
    }
}
