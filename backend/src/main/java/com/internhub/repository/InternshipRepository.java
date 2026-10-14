package com.internhub.repository;

import com.internhub.entity.Internship;
import com.internhub.entity.InternshipStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InternshipRepository extends JpaRepository<Internship, Long>, JpaSpecificationExecutor<Internship> {
    Page<Internship> findByStatus(InternshipStatus status, Pageable pageable);
    Page<Internship> findByCompanyId(Long companyId, Pageable pageable);
    Page<Internship> findByStatusAndTitleContainingIgnoreCase(InternshipStatus status, String title, Pageable pageable);
    Optional<Internship> findByIdAndCompanyId(Long id, Long companyId);
    long countByStatus(com.internhub.entity.InternshipStatus status);
}
