package com.internhub.dto;
import com.internhub.entity.Application;
import java.time.Instant;
import java.util.List;
public record ApplicationResponse(Long id, Long internshipId, String internshipTitle, String companyName, String status, Instant appliedAt, Instant updatedAt, List<StatusEvent> history) {
    public record StatusEvent(String status, Instant changedAt) {}
    public static ApplicationResponse from(Application a) { return new ApplicationResponse(a.getId(), a.getInternship().getId(), a.getInternship().getTitle(), a.getInternship().getCompany().getCompanyName(), a.getStatus().name(), a.getAppliedAt(), a.getUpdatedAt(), a.getStatusHistory().stream().map(event -> new StatusEvent(event.getStatus().name(), event.getChangedAt())).toList()); }
}
