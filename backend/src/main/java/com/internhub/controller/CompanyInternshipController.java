package com.internhub.controller;

import com.internhub.dto.InternshipRequest;
import com.internhub.dto.InternshipResponse;
import com.internhub.security.DatabaseUserPrincipal;
import com.internhub.service.InternshipService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/companies/me/internships")
public class CompanyInternshipController {
    private final InternshipService service;
    public CompanyInternshipController(InternshipService service) { this.service = service; }

    @GetMapping
    public Page<InternshipResponse> list(@AuthenticationPrincipal DatabaseUserPrincipal principal,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return service.listCompany(principal.getId(), PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50), Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InternshipResponse create(@AuthenticationPrincipal DatabaseUserPrincipal principal, @Valid @RequestBody InternshipRequest request) {
        return service.create(principal.getId(), request);
    }

    @PatchMapping("/{internshipId}/publish")
    public InternshipResponse publish(@AuthenticationPrincipal DatabaseUserPrincipal principal, @PathVariable Long internshipId) {
        return service.publish(principal.getId(), internshipId);
    }
}
