package com.internhub.config;

import com.internhub.entity.AccountStatus;
import com.internhub.entity.User;
import com.internhub.entity.UserRole;
import com.internhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class AdminBootstrap {
    @Bean
    ApplicationRunner provisionAdmin(UserRepository users, PasswordEncoder encoder,
                                     @Value("${ADMIN_BOOTSTRAP_EMAIL:}") String email,
                                     @Value("${ADMIN_BOOTSTRAP_PASSWORD:}") String password) {
        return args -> createIfConfigured(users, encoder, email, password);
    }

    @Transactional
    void createIfConfigured(UserRepository users, PasswordEncoder encoder, String configuredEmail, String password) {
        if (configuredEmail == null || configuredEmail.isBlank() || password == null || password.isBlank()) return;
        String email = configuredEmail.trim().toLowerCase(java.util.Locale.ROOT);
        users.findByEmail(email).ifPresentOrElse(existing -> {
            if (existing.getRole() != UserRole.ADMIN) throw new IllegalStateException("ADMIN_BOOTSTRAP_EMAIL belongs to a non-admin account; refusing to change its role.");
        }, () -> {
            if (password.length() < 8) throw new IllegalStateException("ADMIN_BOOTSTRAP_PASSWORD must be at least 8 characters.");
            User admin = new User(); admin.setFullName("Platform Administrator"); admin.setEmail(email); admin.setPasswordHash(encoder.encode(password)); admin.setRole(UserRole.ADMIN); admin.setAccountStatus(AccountStatus.ACTIVE); users.save(admin);
        });
    }
}
