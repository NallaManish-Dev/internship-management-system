package com.internhub.service;
import com.internhub.dto.ApplicationResponse;
import com.internhub.entity.*;
import com.internhub.exception.*;
import com.internhub.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
@Service public class CompanyApplicationService {
 private final ApplicationRepository applications; private final CompanyProfileRepository companies; private final InternshipRepository internships;
 public CompanyApplicationService(ApplicationRepository a,CompanyProfileRepository c,InternshipRepository i){applications=a;companies=c;internships=i;}
 private Long companyId(Long userId){return companies.findByUserId(userId).orElseThrow(()->new ResourceNotFoundException("Company profile not found.")).getId();}
 @Transactional(readOnly=true) public Page<ApplicationResponse> list(Long userId,Long internshipId,Pageable p){Long cid=companyId(userId); if(!internships.findByIdAndCompanyId(internshipId,cid).isPresent()) throw new ResourceNotFoundException("Internship not found."); return applications.findByInternshipIdAndInternshipCompanyId(internshipId,cid,p).map(ApplicationResponse::from);}
 @Transactional(readOnly=true) public ApplicationResponse get(Long userId,Long id){return applications.findByIdAndInternshipCompanyId(id,companyId(userId)).map(ApplicationResponse::from).orElseThrow(()->new ResourceNotFoundException("Application not found."));}
 @Transactional public ApplicationResponse update(Long userId,Long id,ApplicationStatus next){var a=applications.findByIdAndInternshipCompanyId(id,companyId(userId)).orElseThrow(()->new ResourceNotFoundException("Application not found.")); if(!allowed(a.getStatus(),next)) throw new InvalidStatusTransitionException(); a.setStatus(next);a.setUpdatedAt(Instant.now()); var event=new ApplicationStatusHistory(); event.setApplication(a); event.setStatus(next); event.setChangedAt(a.getUpdatedAt()); a.getStatusHistory().add(event); return ApplicationResponse.from(applications.save(a));}
 private boolean allowed(ApplicationStatus current,ApplicationStatus next){return switch(current){case APPLIED->next==ApplicationStatus.UNDER_REVIEW||next==ApplicationStatus.SHORTLISTED||next==ApplicationStatus.REJECTED;case UNDER_REVIEW->next==ApplicationStatus.SHORTLISTED||next==ApplicationStatus.REJECTED;case SHORTLISTED->next==ApplicationStatus.SELECTED||next==ApplicationStatus.REJECTED;case SELECTED,REJECTED->false;};}
}
