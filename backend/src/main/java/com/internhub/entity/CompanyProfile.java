package com.internhub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity @Table(name = "company_profiles", uniqueConstraints = @UniqueConstraint(name = "uk_company_profile_user", columnNames = "user_id"))
public class CompanyProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @OneToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id", nullable = false) private User user;
    @NotBlank @Size(max = 180) @Column(name = "company_name", nullable = false, length = 180) private String companyName;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(length = 255) private String website;
    @Column(length = 100) private String industry;
    @Column(name = "contact_information", length = 255) private String contactInformation;
    public Long getId() { return id; } public User getUser() { return user; } public void setUser(User v) { user = v; } public String getCompanyName() { return companyName; } public void setCompanyName(String v) { companyName = v; } public String getDescription() { return description; } public void setDescription(String v) { description = v; } public String getWebsite() { return website; } public void setWebsite(String v) { website = v; } public String getIndustry() { return industry; } public void setIndustry(String v) { industry = v; } public String getContactInformation() { return contactInformation; } public void setContactInformation(String v) { contactInformation = v; }
}
