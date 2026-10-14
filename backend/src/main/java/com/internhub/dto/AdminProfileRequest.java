package com.internhub.dto;
import jakarta.validation.constraints.NotBlank; import jakarta.validation.constraints.Size;
public record AdminProfileRequest(@NotBlank @Size(max=120) String name) {}
