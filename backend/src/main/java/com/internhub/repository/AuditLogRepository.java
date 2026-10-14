package com.internhub.repository;
import com.internhub.entity.AuditLog; import org.springframework.data.domain.*; import org.springframework.data.mongodb.repository.MongoRepository;
public interface AuditLogRepository extends MongoRepository<AuditLog,Long>{ Page<AuditLog> findAllByOrderByCreatedAtDesc(Pageable pageable); }
