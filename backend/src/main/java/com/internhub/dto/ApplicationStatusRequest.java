package com.internhub.dto;
import com.internhub.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
public record ApplicationStatusRequest(@NotNull ApplicationStatus status) {}
