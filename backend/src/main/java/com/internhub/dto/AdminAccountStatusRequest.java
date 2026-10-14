package com.internhub.dto;
import com.internhub.entity.AccountStatus;
import jakarta.validation.constraints.NotNull;
public record AdminAccountStatusRequest(@NotNull AccountStatus status) {}
