package com.internhub.repository;
import com.internhub.entity.AuditLog; import org.springframework.data.domain.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface AuditLogRepository extends JpaRepository<AuditLog,Long>{ Page<AuditLog> findAllByOrderByCreatedAtDesc(Pageable pageable); }
