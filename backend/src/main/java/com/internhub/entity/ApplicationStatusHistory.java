package com.internhub.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "application_status_history", indexes = @Index(name = "idx_history_application_changed", columnList = "application_id, changed_at"))
public class ApplicationStatusHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "application_id", nullable = false) private Application application;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private ApplicationStatus status;
    @Column(name = "changed_at", nullable = false) private Instant changedAt;
    public Long getId() { return id; }
    public Application getApplication() { return application; }
    public void setApplication(Application value) { application = value; }
    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus value) { status = value; }
    public Instant getChangedAt() { return changedAt; }
    public void setChangedAt(Instant value) { changedAt = value; }
}
