package com.internhub.dto;
import java.math.BigDecimal; import java.util.List;
public final class ProfileResponses { private ProfileResponses(){} public record Student(Object user, String phone,String institution,String degreeOrBranch,Integer graduationYear,BigDecimal cgpa,List<String> skills){} public record Company(Object user,String companyName,String description,String website,String industry,String contactInformation){} public record Dashboard(long totalApplications,long totalInternships,long totalApplicants){} }
