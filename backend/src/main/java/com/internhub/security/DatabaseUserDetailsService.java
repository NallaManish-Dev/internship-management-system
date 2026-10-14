package com.internhub.security;

import com.internhub.entity.User;
import com.internhub.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public DatabaseUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return DatabaseUserPrincipal.from(findByEmail(username));
    }

    public DatabaseUserPrincipal loadById(Long id) {
        return DatabaseUserPrincipal.from(userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found.")));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email.trim().toLowerCase()).orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }
}
