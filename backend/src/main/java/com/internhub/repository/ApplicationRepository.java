package com.internhub.repository;

import com.internhub.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface ApplicationRepository extends MongoRepository<Application, Long> {
    boolean existsByStudentIdAndInternshipId(Long studentId, Long internshipId);
    Page<Application> findByStudentId(Long studentId, Pageable pageable);
    Page<Application> findByInternshipId(Long internshipId, Pageable pageable);
    Optional<Application> findByIdAndStudentId(Long id, Long studentId);
    Page<Application> findByInternshipIdAndCompanyId(Long internshipId, Long companyId, Pageable pageable);
    Optional<Application> findByIdAndCompanyId(Long id, Long companyId);
    long count();
    long countByCompanyId(Long companyId);
    boolean existsByInternshipId(Long internshipId);
}
