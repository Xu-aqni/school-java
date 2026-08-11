package com.budgetapp.controller;

import com.budgetapp.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpServletRequest request) {
        return SessionUtil.isLoggedIn(request) ? "redirect:/dashboard" : "redirect:/login";
    }
}
