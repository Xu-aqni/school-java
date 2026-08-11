package com.budgetapp.service;

import com.budgetapp.model.User;
import com.budgetapp.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registra un nuovo utente. Ritorna Optional.empty() se l'email è già in uso
     * (equivalente del return false in User::register()).
     */
    public Optional<User> register(String name, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            return Optional.empty();
        }
        User user = new User(name, email, passwordEncoder.encode(password));
        return Optional.of(userRepository.save(user));
    }

    /**
     * Equivalente di User::attemptLogin().
     */
    public Optional<User> attemptLogin(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()));
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User updateProfile(Long id, String name, String email) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        user.setName(name);
        user.setEmail(email);
        return userRepository.save(user);
    }
}
