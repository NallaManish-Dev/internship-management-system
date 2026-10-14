package com.internhub.service;

import com.internhub.dto.AdminDtos;
import com.internhub.entity.UserRole;
import com.internhub.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.internhub.exception.ResourceNotFoundException;
import com.internhub.entity.AccountStatus;

@Service
public class AdminService {
    private final UserRepository users; private final CompanyProfileRepository companies; private final InternshipRepository internships; private final ApplicationRepository applications; private final AuditService audit;
    public AdminService(UserRepository users, CompanyProfileRepository companies, InternshipRepository internships, ApplicationRepository applications, AuditService audit) { this.users=users; this.companies=companies; this.internships=internships; this.applications=applications; this.audit=audit; }
    @Transactional(readOnly=true) public AdminDtos.Dashboard dashboard() { return new AdminDtos.Dashboard(users.countByRole(UserRole.STUDENT), users.countByRole(UserRole.COMPANY), internships.count(), applications.count()); }
    @Transactional(readOnly=true) public Page<AdminDtos.UserRow> students(Pageable p) { return users.findByRole(UserRole.STUDENT,p).map(AdminDtos.UserRow::from); }
    @Transactional(readOnly=true) public Page<AdminDtos.CompanyRow> companies(Pageable p) { return companies.findAll(p).map(AdminDtos.CompanyRow::from); }
    @Transactional(readOnly=true) public Page<AdminDtos.InternshipRow> internships(Pageable p) { return internships.findAll(p).map(AdminDtos.InternshipRow::from); }
    @Transactional(readOnly=true) public Page<AdminDtos.ApplicationRow> applications(Pageable p) { return applications.findAll(p).map(AdminDtos.ApplicationRow::from); }
    @Transactional public com.internhub.dto.UserSummary updateProfile(Long id, String name) { var user=users.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found.")); user.setFullName(name.trim()); return com.internhub.dto.UserSummary.from(users.save(user)); }
    @Transactional public AdminDtos.UserRow updateAccountStatus(Long adminId, Long id, AccountStatus status) { var user=users.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found.")); if(user.getRole()==UserRole.ADMIN && status==AccountStatus.INACTIVE) throw new IllegalStateException("The administrator account cannot be deactivated here."); user.setAccountStatus(status); var saved=users.save(user); audit.record(adminId,"ACCOUNT_STATUS_CHANGED","USER",id,status.name()); return AdminDtos.UserRow.from(saved); }
    @Transactional public AdminDtos.InternshipRow closeInternship(Long adminId, Long id) { var internship=internships.findById(id).orElseThrow(()->new ResourceNotFoundException("Internship not found.")); internship.setStatus(com.internhub.entity.InternshipStatus.CLOSED); internship.setUpdatedAt(java.time.Instant.now()); var saved=internships.save(internship); audit.record(adminId,"INTERNSHIP_CLOSED","INTERNSHIP",id,null); return AdminDtos.InternshipRow.from(saved); }
    @Transactional public void deleteInternship(Long adminId, Long id) { if(!internships.existsById(id)) throw new ResourceNotFoundException("Internship not found."); if(applications.existsByInternshipId(id)) throw new IllegalStateException("Internships with applications cannot be deleted; close them instead."); internships.deleteById(id); audit.record(adminId,"INTERNSHIP_DELETED","INTERNSHIP",id,null); }
}
