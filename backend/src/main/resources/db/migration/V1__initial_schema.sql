CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    full_name VARCHAR(120) NOT NULL,
    email VARCHAR(254) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    account_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT ck_users_role CHECK (role IN ('STUDENT', 'COMPANY', 'ADMIN')),
    CONSTRAINT ck_users_account_status CHECK (account_status IN ('ACTIVE', 'INACTIVE'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE student_profiles (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    phone VARCHAR(30), institution VARCHAR(180), degree_or_branch VARCHAR(120),
    graduation_year INT, cgpa DECIMAL(4,2),
    PRIMARY KEY (id), CONSTRAINT uk_student_profile_user UNIQUE (user_id),
    CONSTRAINT fk_student_profile_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE student_skills (
    student_profile_id BIGINT NOT NULL,
    skill VARCHAR(80) NOT NULL,

    PRIMARY KEY (student_profile_id, skill),

    CONSTRAINT fk_student_skill_profile
        FOREIGN KEY (student_profile_id)
        REFERENCES student_profiles(id)
        ON DELETE CASCADE,

    INDEX idx_student_skills_profile (student_profile_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE company_profiles (
    id BIGINT NOT NULL AUTO_INCREMENT, user_id BIGINT NOT NULL, company_name VARCHAR(180) NOT NULL,
    description TEXT, website VARCHAR(255), industry VARCHAR(100), contact_information VARCHAR(255),
    PRIMARY KEY (id), CONSTRAINT uk_company_profile_user UNIQUE (user_id),
    CONSTRAINT fk_company_profile_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE internships (
    id BIGINT NOT NULL AUTO_INCREMENT, company_id BIGINT NOT NULL, title VARCHAR(180) NOT NULL,
    description TEXT NOT NULL, location VARCHAR(160), work_mode VARCHAR(30), duration VARCHAR(80),
    stipend_amount DECIMAL(12,2), stipend_currency VARCHAR(3), eligibility TEXT,
    application_deadline DATE, status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL, updated_at TIMESTAMP(6) NOT NULL,
    PRIMARY KEY (id), CONSTRAINT fk_internship_company FOREIGN KEY (company_id) REFERENCES company_profiles(id),
    INDEX idx_internships_status (status), INDEX idx_internships_company (company_id), INDEX idx_internships_deadline (application_deadline),
    CONSTRAINT ck_internship_status CHECK (status IN ('DRAFT', 'OPEN', 'CLOSED')),
    CONSTRAINT ck_internship_stipend CHECK (stipend_amount IS NULL OR stipend_amount >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE internship_skills (
    internship_id BIGINT NOT NULL,
    skill VARCHAR(80) NOT NULL,

    PRIMARY KEY (internship_id, skill),

    CONSTRAINT fk_internship_skill_internship
        FOREIGN KEY (internship_id)
        REFERENCES internships(id)
        ON DELETE CASCADE,

    INDEX idx_internship_skills_internship (internship_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE applications (
    id BIGINT NOT NULL AUTO_INCREMENT, student_id BIGINT NOT NULL, internship_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL, applied_at TIMESTAMP(6) NOT NULL, updated_at TIMESTAMP(6) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id), CONSTRAINT uk_application_student_internship UNIQUE (student_id, internship_id),
    CONSTRAINT fk_application_student FOREIGN KEY (student_id) REFERENCES student_profiles(id),
    CONSTRAINT fk_application_internship FOREIGN KEY (internship_id) REFERENCES internships(id),
    INDEX idx_applications_student (student_id), INDEX idx_applications_internship (internship_id), INDEX idx_applications_status (status),
    CONSTRAINT ck_application_status CHECK (status IN ('APPLIED', 'UNDER_REVIEW', 'SHORTLISTED', 'SELECTED', 'REJECTED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
