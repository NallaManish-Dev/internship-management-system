package com.internhub.repository;

import com.internhub.entity.StudentProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface StudentProfileRepository extends MongoRepository<StudentProfile, Long> {
    Optional<StudentProfile> findByUserId(Long userId);
}
