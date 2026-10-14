package com.internhub.repository;

import com.internhub.entity.CompanyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyProfileRepository extends JpaRepository<CompanyProfile, Long> {
    Optional<CompanyProfile> findByUserId(Long userId);
    Page<CompanyProfile> findAll(Pageable pageable);
}
