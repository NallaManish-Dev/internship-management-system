package com.internhub.dto;

import com.internhub.entity.Internship;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record InternshipResponse(Long id, String title, String description, CompanySummary company,
                                 String location, String workMode, String duration, BigDecimal stipendAmount,
                                 String stipendCurrency, List<String> skills, String eligibility,
                                 LocalDate applicationDeadline, String status, Instant createdAt, Instant updatedAt) {
    public static InternshipResponse from(Internship internship) {
        var company = internship.getCompany();
        return new InternshipResponse(internship.getId(), internship.getTitle(), internship.getDescription(),
                new CompanySummary(company.getId(), company.getCompanyName()), internship.getLocation(),
                internship.getWorkMode(), internship.getDuration(), internship.getStipendAmount(),
                internship.getStipendCurrency(), List.copyOf(internship.getSkills()), internship.getEligibility(),
                internship.getApplicationDeadline(), internship.getStatus().name(), internship.getCreatedAt(), internship.getUpdatedAt());
    }
    public record CompanySummary(Long id, String name) {}
}
