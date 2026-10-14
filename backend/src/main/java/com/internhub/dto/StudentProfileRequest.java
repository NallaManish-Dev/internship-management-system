package com.internhub.dto;
import jakarta.validation.constraints.*; import java.math.BigDecimal; import java.util.List;
public record StudentProfileRequest(@NotBlank @Size(max=120) String name,@Size(max=30) String phone,@Size(max=180) String institution,@Size(max=120) String degreeOrBranch,@Min(1900) @Max(2200) Integer graduationYear,@DecimalMin("0.0") @DecimalMax("10.0") BigDecimal cgpa,List<@Size(max=80) String> skills) {}
