package com.internhub.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "internships", indexes = {@Index(name = "idx_internships_status", columnList = "status"), @Index(name = "idx_internships_company", columnList = "company_id"), @Index(name = "idx_internships_deadline", columnList = "application_deadline")})
public class Internship {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "company_id", nullable = false) private CompanyProfile company;
    @NotBlank @Size(max = 180) @Column(nullable = false, length = 180) private String title;
    @NotBlank @Column(nullable = false, columnDefinition = "TEXT") private String description;
    @Column(length = 160) private String location;
    @Column(name = "work_mode", length = 30) private String workMode;
    @Column(length = 80) private String duration;
    @DecimalMin(value = "0.0", inclusive = true) @Column(name = "stipend_amount", precision = 12, scale = 2) private BigDecimal stipendAmount;
    @Column(name = "stipend_currency", length = 3) private String stipendCurrency;
    @ElementCollection @CollectionTable(name = "internship_skills", joinColumns = @JoinColumn(name = "internship_id")) @Column(name = "skill", nullable = false, length = 80) private List<String> skills = new ArrayList<>();
    @Column(columnDefinition = "TEXT") private String eligibility;
    @Column(name = "application_deadline") private LocalDate applicationDeadline;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private InternshipStatus status;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt;
    public Long getId() { return id; } public CompanyProfile getCompany() { return company; } public void setCompany(CompanyProfile v) { company = v; } public String getTitle() { return title; } public void setTitle(String v) { title = v; } public String getDescription() { return description; } public void setDescription(String v) { description = v; } public String getLocation() { return location; } public void setLocation(String v) { location = v; } public String getWorkMode() { return workMode; } public void setWorkMode(String v) { workMode = v; } public String getDuration() { return duration; } public void setDuration(String v) { duration = v; } public BigDecimal getStipendAmount() { return stipendAmount; } public void setStipendAmount(BigDecimal v) { stipendAmount = v; } public String getStipendCurrency() { return stipendCurrency; } public void setStipendCurrency(String v) { stipendCurrency = v; } public List<String> getSkills() { return skills; } public String getEligibility() { return eligibility; } public void setEligibility(String v) { eligibility = v; } public LocalDate getApplicationDeadline() { return applicationDeadline; } public void setApplicationDeadline(LocalDate v) { applicationDeadline = v; } public InternshipStatus getStatus() { return status; } public void setStatus(InternshipStatus v) { status = v; } public Instant getCreatedAt() { return createdAt; } public void setCreatedAt(Instant v) { createdAt = v; } public Instant getUpdatedAt() { return updatedAt; } public void setUpdatedAt(Instant v) { updatedAt = v; }
}
