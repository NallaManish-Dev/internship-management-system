package com.internhub.entity;
import jakarta.validation.constraints.DecimalMax; import jakarta.validation.constraints.DecimalMin; import jakarta.validation.constraints.Max; import jakarta.validation.constraints.Min;
import org.springframework.data.annotation.Id; import org.springframework.data.mongodb.core.index.Indexed; import org.springframework.data.mongodb.core.mapping.DBRef; import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList; import java.util.List;
@Document("student_profiles") public class StudentProfile {
 @Id private Long id; @Indexed(unique=true) private Long userId; @DBRef private User user;
 private String phone; private String institution; private String degreeOrBranch; @Min(1900) @Max(2200) private Integer graduationYear; @DecimalMin("0.0") @DecimalMax("10.0") private java.math.BigDecimal cgpa; private List<String> skills=new ArrayList<>();
 public Long getId(){return id;} public void setId(Long v){id=v;} public Long getUserId(){return userId;} public User getUser(){return user;} public void setUser(User v){user=v;userId=v==null?null:v.getId();} public String getPhone(){return phone;} public void setPhone(String v){phone=v;} public String getInstitution(){return institution;} public void setInstitution(String v){institution=v;} public String getDegreeOrBranch(){return degreeOrBranch;} public void setDegreeOrBranch(String v){degreeOrBranch=v;} public Integer getGraduationYear(){return graduationYear;} public void setGraduationYear(Integer v){graduationYear=v;} public java.math.BigDecimal getCgpa(){return cgpa;} public void setCgpa(java.math.BigDecimal v){cgpa=v;} public List<String> getSkills(){return skills;}
}
