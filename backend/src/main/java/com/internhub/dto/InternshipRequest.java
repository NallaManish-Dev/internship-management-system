package com.internhub.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record InternshipRequest(
        @NotBlank @Size(max = 180) String title,
        @NotBlank String description,
        @Size(max = 160) String location,
        @Size(max = 30) String workMode,
        @Size(max = 80) String duration,
        @DecimalMin(value = "0.0", inclusive = true) BigDecimal stipendAmount,
        @Size(max = 3) String stipendCurrency,
        List<@NotBlank @Size(max = 80) String> skills,
        String eligibility,
        LocalDate applicationDeadline
) {}
