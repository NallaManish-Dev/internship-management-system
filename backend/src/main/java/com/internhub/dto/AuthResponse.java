package com.internhub.dto;

public record AuthResponse(String accessToken, String tokenType, long expiresIn, UserSummary user) {
}
