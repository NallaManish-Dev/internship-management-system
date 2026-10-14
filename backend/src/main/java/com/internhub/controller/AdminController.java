package com.internhub.controller;

import com.internhub.dto.AdminDtos;
import com.internhub.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.internhub.dto.AdminAccountStatusRequest;
import com.internhub.security.DatabaseUserPrincipal;
import com.internhub.service.AuditService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService service; private final AuditService audit;
    public AdminController(AdminService service, AuditService audit) { this.service=service; this.audit=audit; }
    private Pageable page(int number, int size) { return PageRequest.of(Math.max(number,0), Math.min(Math.max(size,1),50), Sort.by(Sort.Direction.DESC,"id")); }
    @GetMapping("/dashboard") public AdminDtos.Dashboard dashboard() { return service.dashboard(); }
    @GetMapping("/students") public Page<AdminDtos.UserRow> students(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="10") int size) { return service.students(page(page,size)); }
    @GetMapping("/companies") public Page<AdminDtos.CompanyRow> companies(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="10") int size) { return service.companies(page(page,size)); }
    @GetMapping("/internships") public Page<AdminDtos.InternshipRow> internships(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="10") int size) { return service.internships(page(page,size)); }
    @GetMapping("/applications") public Page<AdminDtos.ApplicationRow> applications(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="10") int size) { return service.applications(page(page,size)); }
    @PatchMapping("/users/{id}/status") public AdminDtos.UserRow updateUserStatus(@AuthenticationPrincipal DatabaseUserPrincipal admin,@PathVariable Long id,@Valid @RequestBody AdminAccountStatusRequest request) { return service.updateAccountStatus(admin.getId(), id, request.status()); }
    @PatchMapping("/internships/{id}/close") public AdminDtos.InternshipRow closeInternship(@AuthenticationPrincipal DatabaseUserPrincipal admin,@PathVariable Long id) { return service.closeInternship(admin.getId(), id); }
    @DeleteMapping("/internships/{id}") @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT) public void deleteInternship(@AuthenticationPrincipal DatabaseUserPrincipal admin,@PathVariable Long id) { service.deleteInternship(admin.getId(), id); }
    @GetMapping("/audit-logs") public org.springframework.data.domain.Page<AdminDtos.AuditRow> auditLogs(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size) { return audit.list(PageRequest.of(Math.max(0,page),Math.min(50,Math.max(1,size)))); }
    @PutMapping("/me") public com.internhub.dto.UserSummary updateProfile(@AuthenticationPrincipal DatabaseUserPrincipal admin,@Valid @RequestBody com.internhub.dto.AdminProfileRequest request) { return service.updateProfile(admin.getId(), request.name()); }
}
