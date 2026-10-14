CREATE TABLE audit_logs (
 id BIGINT NOT NULL AUTO_INCREMENT,
 admin_user_id BIGINT NOT NULL,
 action VARCHAR(80) NOT NULL,
 target_type VARCHAR(40) NOT NULL,
 target_id BIGINT,
 details VARCHAR(500),
 created_at TIMESTAMP(6) NOT NULL,
 PRIMARY KEY (id),
 CONSTRAINT fk_audit_admin FOREIGN KEY (admin_user_id) REFERENCES users(id),
 INDEX idx_audit_created_at (created_at), INDEX idx_audit_admin (admin_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
