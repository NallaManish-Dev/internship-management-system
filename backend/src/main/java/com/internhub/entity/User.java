package com.internhub.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document("users")
public class User {
    @Id private Long id;
    @NotBlank @Size(max = 120) private String fullName;
    @NotBlank @Email @Size(max = 254) @Indexed(unique = true) private String email;
    private String passwordHash;
    private UserRole role;
    private AccountStatus accountStatus = AccountStatus.ACTIVE;
    private Instant createdAt;
    private Instant updatedAt;
    public Long getId() { return id; } public void setId(Long value) { id = value; }
    public String getFullName() { return fullName; } public void setFullName(String v) { fullName = v; }
    public String getEmail() { return email; } public void setEmail(String v) { email = v; } public String getPasswordHash() { return passwordHash; } public void setPasswordHash(String v) { passwordHash = v; }
    public UserRole getRole() { return role; } public void setRole(UserRole v) { role = v; } public AccountStatus getAccountStatus() { return accountStatus; } public void setAccountStatus(AccountStatus v) { accountStatus = v; }
    public Instant getCreatedAt() { return createdAt; } public void setCreatedAt(Instant value) { createdAt = value; }
    public Instant getUpdatedAt() { return updatedAt; } public void setUpdatedAt(Instant value) { updatedAt = value; }
}
