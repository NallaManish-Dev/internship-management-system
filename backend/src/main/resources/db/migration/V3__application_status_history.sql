CREATE TABLE application_status_history (
 id BIGINT NOT NULL AUTO_INCREMENT,
 application_id BIGINT NOT NULL,
 status VARCHAR(20) NOT NULL,
 changed_at TIMESTAMP(6) NOT NULL,
 PRIMARY KEY (id),
 CONSTRAINT fk_history_application FOREIGN KEY (application_id) REFERENCES applications(id),
 INDEX idx_history_application_changed (application_id, changed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO application_status_history (application_id, status, changed_at)
 SELECT id, status, applied_at FROM applications;
