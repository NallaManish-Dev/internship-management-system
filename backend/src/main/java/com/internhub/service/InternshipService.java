package com.internhub.service;

import com.internhub.dto.InternshipRequest;
import com.internhub.dto.InternshipResponse;
import com.internhub.entity.Internship;
import com.internhub.entity.InternshipStatus;
import com.internhub.exception.ResourceNotFoundException;
import com.internhub.repository.CompanyProfileRepository;
import com.internhub.repository.InternshipRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InternshipService {
    private final InternshipRepository internships;
    private final CompanyProfileRepository companies;

    public InternshipService(InternshipRepository internships, CompanyProfileRepository companies) {
        this.internships = internships;
        this.companies = companies;
    }

    @Transactional(readOnly = true)
    public Page<InternshipResponse> listPublic(String query, String location, String workMode, Pageable pageable) {
        Specification<Internship> specification = (root, criteriaQuery, builder) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();
            predicates.add(builder.equal(root.get("status"), InternshipStatus.OPEN));
            if (query != null && !query.isBlank()) {
                String term = "%" + query.trim().toLowerCase(java.util.Locale.ROOT) + "%";
                var company = root.join("company");
                predicates.add(builder.or(builder.like(builder.lower(root.get("title")), term), builder.like(builder.lower(root.get("description")), term), builder.like(builder.lower(root.get("location")), term), builder.like(builder.lower(company.get("companyName")), term)));
            }
            if (location != null && !location.isBlank()) predicates.add(builder.like(builder.lower(root.get("location")), "%" + location.trim().toLowerCase(java.util.Locale.ROOT) + "%"));
            if (workMode != null && !workMode.isBlank()) predicates.add(builder.equal(builder.lower(root.get("workMode")), workMode.trim().toLowerCase(java.util.Locale.ROOT)));
            return builder.and(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
        return internships.findAll(specification, pageable).map(InternshipResponse::from);
    }

    @Transactional(readOnly = true)
    public InternshipResponse getPublic(Long id) {
        Internship internship = internships.findById(id)
                .filter(value -> value.getStatus() == InternshipStatus.OPEN)
                .orElseThrow(() -> new ResourceNotFoundException("Internship not found."));
        return InternshipResponse.from(internship);
    }

    @Transactional(readOnly = true)
    public Page<InternshipResponse> listCompany(Long userId, Pageable pageable) {
        var company = companies.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Company profile not found."));
        return internships.findByCompanyId(company.getId(), pageable).map(InternshipResponse::from);
    }

    @Transactional
    public InternshipResponse create(Long userId, InternshipRequest request) {
        var company = companies.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Company profile not found."));
        Internship internship = new Internship();
        internship.setCompany(company);
        apply(internship, request);
        internship.setStatus(InternshipStatus.DRAFT);
        internship.setCreatedAt(java.time.Instant.now());
        internship.setUpdatedAt(internship.getCreatedAt());
        return InternshipResponse.from(internships.save(internship));
    }

    @Transactional
    public InternshipResponse publish(Long userId, Long internshipId) {
        var company = companies.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Company profile not found."));
        Internship internship = internships.findByIdAndCompanyId(internshipId, company.getId()).orElseThrow(() -> new ResourceNotFoundException("Internship not found."));
        if (internship.getStatus() == InternshipStatus.CLOSED) throw new IllegalStateException("Closed internships cannot be published.");
        if (internship.getApplicationDeadline() != null && internship.getApplicationDeadline().isBefore(java.time.LocalDate.now())) throw new IllegalStateException("Application deadline must be today or later.");
        internship.setStatus(InternshipStatus.OPEN);
        internship.setUpdatedAt(java.time.Instant.now());
        return InternshipResponse.from(internships.save(internship));
    }

    private void apply(Internship internship, InternshipRequest request) {
        internship.setTitle(request.title().trim());
        internship.setDescription(request.description().trim());
        internship.setLocation(blankToNull(request.location()));
        internship.setWorkMode(blankToNull(request.workMode()));
        internship.setDuration(blankToNull(request.duration()));
        internship.setStipendAmount(request.stipendAmount());
        internship.setStipendCurrency(blankToNull(request.stipendCurrency()));
        internship.getSkills().clear();
        if (request.skills() != null) internship.getSkills().addAll(request.skills().stream().map(String::trim).filter(value -> !value.isBlank()).distinct().toList());
        internship.setEligibility(blankToNull(request.eligibility()));
        internship.setApplicationDeadline(request.applicationDeadline());
    }

    private String blankToNull(String value) { return value == null || value.isBlank() ? null : value.trim(); }
}
