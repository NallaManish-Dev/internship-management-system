package com.internhub.service;
import com.internhub.dto.ApplicationResponse;
import com.internhub.entity.*;
import com.internhub.exception.*;
import com.internhub.repository.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
@Service public class ApplicationService {
 private final ApplicationRepository applications; private final StudentProfileRepository students; private final InternshipRepository internships;
 public ApplicationService(ApplicationRepository a, StudentProfileRepository s, InternshipRepository i){applications=a;students=s;internships=i;}
 @Transactional public ApplicationResponse apply(Long userId, Long internshipId){ var student=students.findByUserId(userId).orElseThrow(()->new ResourceNotFoundException("Student profile not found.")); var internship=internships.findById(internshipId).orElseThrow(()->new ResourceNotFoundException("Internship not found.")); if(internship.getStatus()!=InternshipStatus.OPEN) throw new InternshipClosedException(); if(applications.existsByStudentIdAndInternshipId(student.getId(), internshipId)) throw new DuplicateApplicationException(); var a=new Application(); a.setStudent(student);a.setInternship(internship);a.setStatus(ApplicationStatus.APPLIED);a.setAppliedAt(Instant.now());a.setUpdatedAt(a.getAppliedAt()); addHistory(a, ApplicationStatus.APPLIED, a.getAppliedAt()); try{return ApplicationResponse.from(applications.saveAndFlush(a));}catch(DataIntegrityViolationException e){throw new DuplicateApplicationException();} }
 @Transactional(readOnly=true) public Page<ApplicationResponse> list(Long userId, Pageable pageable){var student=students.findByUserId(userId).orElseThrow(()->new ResourceNotFoundException("Student profile not found."));return applications.findByStudentId(student.getId(),pageable).map(ApplicationResponse::from);}
 @Transactional(readOnly=true) public ApplicationResponse get(Long userId, Long applicationId){var student=students.findByUserId(userId).orElseThrow(()->new ResourceNotFoundException("Student profile not found."));return applications.findByIdAndStudentId(applicationId,student.getId()).map(ApplicationResponse::from).orElseThrow(()->new ResourceNotFoundException("Application not found."));}
 private void addHistory(Application application, ApplicationStatus status, Instant changedAt){var event=new ApplicationStatusHistory(); event.setApplication(application); event.setStatus(status); event.setChangedAt(changedAt); application.getStatusHistory().add(event);}
}
