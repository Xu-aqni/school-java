package com.budgetapp.config;

import com.budgetapp.repository.UserRepository;
import com.budgetapp.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

/**
 * Crea gli stessi utenti demo citati nella view login.php del progetto originale,
 * così l'applicazione è utilizzabile subito dopo l'avvio.
 */
@Configuration
public class DemoDataLoader {

    @org.springframework.context.annotation.Bean
    CommandLineRunner loadDemoUsers(UserService userService, UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                userService.register("Giovanni Rossi", "giovanni@example.com", "password123");
                userService.register("Maria Bianchi", "maria@example.com", "password123");
                userService.register("Luca Verdi", "luca@example.com", "password123");
            }
        };
    }
}
