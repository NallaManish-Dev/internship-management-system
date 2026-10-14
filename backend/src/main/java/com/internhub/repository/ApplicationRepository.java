package com.internhub.repository;

import com.internhub.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByStudentIdAndInternshipId(Long studentId, Long internshipId);
    Page<Application> findByStudentId(Long studentId, Pageable pageable);
    Page<Application> findByInternshipId(Long internshipId, Pageable pageable);
    Optional<Application> findByIdAndStudentId(Long id, Long studentId);
    Page<Application> findByInternshipIdAndInternshipCompanyId(Long internshipId, Long companyId, Pageable pageable);
    Optional<Application> findByIdAndInternshipCompanyId(Long id, Long companyId);
    long count();
    long countByInternshipCompanyId(Long companyId);
    boolean existsByInternshipId(Long internshipId);
}
