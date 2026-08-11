package com.budgetapp.util;

import com.budgetapp.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Equivalente della classe App\Models\Session del progetto PHP.
 * In Spring Boot la sessione è gestita nativamente da HttpSession; questa classe
 * centralizza le stesse operazioni (login, logout, isLoggedIn, userId) usate nei controller.
 */
public final class SessionUtil {

    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_EMAIL = "user_email";

    private SessionUtil() {
    }

    public static void login(HttpServletRequest request, User user) {
        // Assicura che esista una sessione prima di rigenerarne l'ID
        // (session_start() + session_regenerate_id(true) in PHP)
        request.getSession(true);
        request.changeSessionId();
        HttpSession session = request.getSession(true);
        session.setAttribute(USER_ID, user.getId());
        session.setAttribute(USER_NAME, user.getName());
        session.setAttribute(USER_EMAIL, user.getEmail());
    }

    public static void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public static boolean isLoggedIn(HttpServletRequest request) {
        return userId(request) != null;
    }

    public static Long userId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (Long) session.getAttribute(USER_ID);
    }
}
