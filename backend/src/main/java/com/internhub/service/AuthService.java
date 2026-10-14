package com.internhub.service;

import com.internhub.dto.AuthResponse;
import com.internhub.dto.LoginRequest;
import com.internhub.dto.RegisterCompanyRequest;
import com.internhub.dto.RegisterStudentRequest;
import com.internhub.dto.UserSummary;
import com.internhub.entity.CompanyProfile;
import com.internhub.entity.StudentProfile;
import com.internhub.entity.User;
import com.internhub.entity.UserRole;
import com.internhub.exception.EmailAlreadyExistsException;
import com.internhub.repository.CompanyProfileRepository;
import com.internhub.repository.StudentProfileRepository;
import com.internhub.repository.UserRepository;
import com.internhub.security.JwtService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final CompanyProfileRepository companyProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, StudentProfileRepository studentProfileRepository, CompanyProfileRepository companyProfileRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository; this.studentProfileRepository = studentProfileRepository; this.companyProfileRepository = companyProfileRepository;
        this.passwordEncoder = passwordEncoder; this.authenticationManager = authenticationManager; this.jwtService = jwtService;
    }

    @Transactional
    public UserSummary registerStudent(RegisterStudentRequest request) {
        User user = createUser(request.name(), request.email(), request.password(), UserRole.STUDENT);
        StudentProfile profile = new StudentProfile(); profile.setUser(user);
        studentProfileRepository.save(profile);
        return UserSummary.from(user);
    }

    @Transactional
    public UserSummary registerCompany(RegisterCompanyRequest request) {
        User user = createUser(request.companyName(), request.email(), request.password(), UserRole.COMPANY);
        CompanyProfile profile = new CompanyProfile(); profile.setUser(user); profile.setCompanyName(request.companyName());
        companyProfileRepository.save(profile);
        return UserSummary.from(user);
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email().trim().toLowerCase(), request.password()));
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        return new AuthResponse(jwtService.issue(user), "Bearer", jwtService.expiresIn(), UserSummary.from(user));
    }

    @Transactional(readOnly = true)
    public UserSummary currentUser(Authentication authentication) {
        com.internhub.security.DatabaseUserPrincipal principal = (com.internhub.security.DatabaseUserPrincipal) authentication.getPrincipal();
        return UserSummary.from(userRepository.findById(principal.getId()).orElseThrow());
    }

    private User createUser(String name, String email, String password, UserRole role) {
        String normalizedEmail = email.trim().toLowerCase();
        if (userRepository.existsByEmail(normalizedEmail)) throw new EmailAlreadyExistsException();
        User user = new User(); user.setFullName(name.trim()); user.setEmail(normalizedEmail); user.setPasswordHash(passwordEncoder.encode(password)); user.setRole(role);
        try { return userRepository.save(user); }
        catch (DataIntegrityViolationException exception) { throw new EmailAlreadyExistsException(); }
    }
}
