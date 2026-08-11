package com.budgetapp.controller;

import com.budgetapp.model.User;
import com.budgetapp.service.UserService;
import com.budgetapp.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegister() {
        return "user/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                            @RequestParam String email,
                            @RequestParam String password,
                            Model model,
                            HttpServletRequest request) {
        name = name == null ? "" : name.trim();
        email = email == null ? "" : email.trim();

        if (name.isEmpty() || email.isEmpty() || password == null || password.isEmpty()) {
            model.addAttribute("error", "Tutti i campi sono obbligatori.");
            return "user/register";
        }

        Optional<User> created = userService.register(name, email, password);
        if (created.isEmpty()) {
            model.addAttribute("error", "Esiste già un utente con questa email.");
            return "user/register";
        }

        SessionUtil.login(request, created.get());
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                         @RequestParam String password,
                         Model model,
                         HttpServletRequest request) {
        Optional<User> user = userService.attemptLogin(email == null ? "" : email.trim(), password);

        if (user.isEmpty()) {
            model.addAttribute("error", "Email o password non corretti.");
            return "user/login";
        }

        SessionUtil.login(request, user.get());
        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        SessionUtil.logout(request);
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String showProfile(Model model, HttpServletRequest request) {
        User user = userService.findById(SessionUtil.userId(request))
                .orElseThrow(() -> new IllegalStateException("Utente non trovato"));
        model.addAttribute("user", user);
        return "user/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam String name,
                                 @RequestParam String email,
                                 Model model,
                                 HttpServletRequest request) {
        Long userId = SessionUtil.userId(request);
        User updated = userService.updateProfile(userId, name.trim(), email.trim());

        model.addAttribute("user", updated);
        model.addAttribute("success", "Profilo aggiornato con successo.");
        return "user/profile";
    }
}
