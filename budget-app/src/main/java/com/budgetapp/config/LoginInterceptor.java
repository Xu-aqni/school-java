package com.budgetapp.config;

import com.budgetapp.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Equivalente di Session::requireLogin(): reindirizza al login se l'utente non è autenticato.
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!SessionUtil.isLoggedIn(request)) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
