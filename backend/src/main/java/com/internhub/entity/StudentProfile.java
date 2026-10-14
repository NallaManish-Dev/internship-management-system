package com.internhub.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "student_profiles", uniqueConstraints = @UniqueConstraint(name = "uk_student_profile_user", columnNames = "user_id"))
public class StudentProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @OneToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id", nullable = false) private User user;
    @Column(length = 30) private String phone;
    @Column(length = 180) private String institution;
    @Column(name = "degree_or_branch", length = 120) private String degreeOrBranch;
    @Min(1900) @Max(2200) @Column(name = "graduation_year") private Integer graduationYear;
    @DecimalMin("0.0") @DecimalMax("10.0") @Column(precision = 4, scale = 2) private java.math.BigDecimal cgpa;
    @ElementCollection @CollectionTable(name = "student_skills", joinColumns = @JoinColumn(name = "student_profile_id")) @Column(name = "skill", nullable = false, length = 80) private List<String> skills = new ArrayList<>();
    public Long getId() { return id; } public User getUser() { return user; } public void setUser(User v) { user = v; } public String getPhone() { return phone; } public void setPhone(String v) { phone = v; }
    public String getInstitution() { return institution; } public void setInstitution(String v) { institution = v; } public String getDegreeOrBranch() { return degreeOrBranch; } public void setDegreeOrBranch(String v) { degreeOrBranch = v; } public Integer getGraduationYear() { return graduationYear; } public void setGraduationYear(Integer v) { graduationYear = v; } public java.math.BigDecimal getCgpa() { return cgpa; } public void setCgpa(java.math.BigDecimal v) { cgpa = v; } public List<String> getSkills() { return skills; }
}
