package com.internhub.dto;
import jakarta.validation.constraints.*;
public record CompanyProfileRequest(@NotBlank @Size(max=180) String companyName,@Size(max=2000) String description,@Size(max=255) String website,@Size(max=100) String industry,@Size(max=255) String contactInformation) {}
