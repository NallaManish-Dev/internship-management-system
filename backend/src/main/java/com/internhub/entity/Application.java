package com.internhub.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "applications", uniqueConstraints = @UniqueConstraint(name = "uk_application_student_internship", columnNames = {"student_id", "internship_id"}), indexes = {@Index(name = "idx_applications_student", columnList = "student_id"), @Index(name = "idx_applications_internship", columnList = "internship_id"), @Index(name = "idx_applications_status", columnList = "status")})
public class Application {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "student_id", nullable = false) private StudentProfile student;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "internship_id", nullable = false) private Internship internship;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private ApplicationStatus status;
    @Column(name = "applied_at", nullable = false, updatable = false) private Instant appliedAt;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt;
    @Version private Long version;
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("changedAt ASC") private List<ApplicationStatusHistory> statusHistory = new ArrayList<>();
    public Long getId() { return id; } public StudentProfile getStudent() { return student; } public void setStudent(StudentProfile v) { student = v; } public Internship getInternship() { return internship; } public void setInternship(Internship v) { internship = v; } public ApplicationStatus getStatus() { return status; } public void setStatus(ApplicationStatus v) { status = v; } public Instant getAppliedAt() { return appliedAt; } public void setAppliedAt(Instant v) { appliedAt = v; } public Instant getUpdatedAt() { return updatedAt; } public void setUpdatedAt(Instant v) { updatedAt = v; } public Long getVersion() { return version; } public List<ApplicationStatusHistory> getStatusHistory() { return statusHistory; }
}
