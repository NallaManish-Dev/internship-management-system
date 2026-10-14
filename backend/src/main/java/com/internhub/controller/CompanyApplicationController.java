package com.internhub.controller;
import com.internhub.dto.*;
import com.internhub.security.DatabaseUserPrincipal;
import com.internhub.service.CompanyApplicationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/companies/me") public class CompanyApplicationController {
 private final CompanyApplicationService service; public CompanyApplicationController(CompanyApplicationService s){service=s;}
 @GetMapping("/internships/{internshipId}/applications") public Page<ApplicationResponse> list(@AuthenticationPrincipal DatabaseUserPrincipal p,@PathVariable Long internshipId,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="10") int size){return service.list(p.getId(),internshipId,PageRequest.of(Math.max(0,page),Math.min(50,Math.max(1,size)),Sort.by(Sort.Direction.DESC,"appliedAt")));}
 @GetMapping("/applications/{applicationId}") public ApplicationResponse get(@AuthenticationPrincipal DatabaseUserPrincipal p,@PathVariable Long applicationId){return service.get(p.getId(),applicationId);}
 @PatchMapping("/applications/{applicationId}/status") public ApplicationResponse update(@AuthenticationPrincipal DatabaseUserPrincipal p,@PathVariable Long applicationId,@Valid @RequestBody ApplicationStatusRequest request){return service.update(p.getId(),applicationId,request.status());}
}
