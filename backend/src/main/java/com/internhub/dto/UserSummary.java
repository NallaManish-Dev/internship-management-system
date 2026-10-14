package com.internhub.dto;

import com.internhub.entity.User;

public record UserSummary(Long id, String name, String email, String role) {
    public static UserSummary from(User user) {
        return new UserSummary(user.getId(), user.getFullName(), user.getEmail(), user.getRole().name());
    }
}
