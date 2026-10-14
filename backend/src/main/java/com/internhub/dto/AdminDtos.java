package com.internhub.dto;

import com.internhub.entity.*;
import java.time.Instant;

public final class AdminDtos {
    private AdminDtos() {}
    public record Dashboard(long totalStudents, long totalCompanies, long totalInternships, long totalApplications) {}
    public record AuditRow(Long id, String adminEmail, String action, String targetType, Long targetId, String details, java.time.Instant createdAt) { public static AuditRow from(com.internhub.entity.AuditLog l){return new AuditRow(l.getId(),l.getAdmin().getEmail(),l.getAction(),l.getTargetType(),l.getTargetId(),l.getDetails(),l.getCreatedAt());} }
    public record UserRow(Long id, String name, String email, String role, String status, Instant createdAt) {
        public static UserRow from(User u) { return new UserRow(u.getId(), u.getFullName(), u.getEmail(), u.getRole().name(), u.getAccountStatus().name(), u.getCreatedAt()); }
    }
    public record CompanyRow(Long id, String companyName, String email, String status) {
        public static CompanyRow from(CompanyProfile c) { return new CompanyRow(c.getUser().getId(), c.getCompanyName(), c.getUser().getEmail(), c.getUser().getAccountStatus().name()); }
    }
    public record InternshipRow(Long id, String title, Long companyId, String companyName, String status, Instant createdAt) {
        public static InternshipRow from(Internship i) { return new InternshipRow(i.getId(), i.getTitle(), i.getCompany().getId(), i.getCompany().getCompanyName(), i.getStatus().name(), i.getCreatedAt()); }
    }
    public record ApplicationRow(Long id, Long internshipId, String internshipTitle, Long studentId, String studentName, String status, Instant appliedAt) {
        public static ApplicationRow from(Application a) { return new ApplicationRow(a.getId(), a.getInternship().getId(), a.getInternship().getTitle(), a.getStudent().getId(), a.getStudent().getUser().getFullName(), a.getStatus().name(), a.getAppliedAt()); }
    }
}
