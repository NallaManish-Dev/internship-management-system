package com.internhub.controller;

import com.internhub.dto.AuthResponse;
import com.internhub.dto.LoginRequest;
import com.internhub.dto.RegisterCompanyRequest;
import com.internhub.dto.RegisterStudentRequest;
import com.internhub.dto.UserSummary;
import com.internhub.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/register/student")
    public ResponseEntity<UserSummary> registerStudent(@Valid @RequestBody RegisterStudentRequest request) {
        UserSummary created = authService.registerStudent(request);
        return ResponseEntity.created(URI.create("/api/v1/auth/users/" + created.id())).body(created);
    }

    @PostMapping("/register/company")
    public ResponseEntity<UserSummary> registerCompany(@Valid @RequestBody RegisterCompanyRequest request) {
        UserSummary created = authService.registerCompany(request);
        return ResponseEntity.created(URI.create("/api/v1/auth/users/" + created.id())).body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) { return ResponseEntity.ok(authService.login(request)); }

    @GetMapping("/me")
    public ResponseEntity<UserSummary> me(Authentication authentication) { return ResponseEntity.ok(authService.currentUser(authentication)); }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() { return ResponseEntity.noContent().build(); }
}
