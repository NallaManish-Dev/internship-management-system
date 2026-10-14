package com.internhub.repository;

import com.internhub.entity.CompanyProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyProfileRepository extends MongoRepository<CompanyProfile, Long> {
    Optional<CompanyProfile> findByUserId(Long userId);
    Page<CompanyProfile> findAll(Pageable pageable);
}
