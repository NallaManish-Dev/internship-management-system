package com.internhub.controller;
import com.internhub.dto.ApplicationResponse;
import com.internhub.security.DatabaseUserPrincipal;
import com.internhub.service.ApplicationService;
import org.springframework.data.domain.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1") public class StudentApplicationController {
 private final ApplicationService service; public StudentApplicationController(ApplicationService s){service=s;}
 @PostMapping("/internships/{internshipId}/applications") @ResponseStatus(org.springframework.http.HttpStatus.CREATED) public ApplicationResponse apply(@AuthenticationPrincipal DatabaseUserPrincipal p,@PathVariable Long internshipId){return service.apply(p.getId(),internshipId);}
 @GetMapping("/students/me/applications") public Page<ApplicationResponse> list(@AuthenticationPrincipal DatabaseUserPrincipal p,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="10") int size){return service.list(p.getId(),PageRequest.of(Math.max(0,page),Math.min(50,Math.max(1,size)),Sort.by(Sort.Direction.DESC,"appliedAt")));}
 @GetMapping("/students/me/applications/{applicationId}") public ApplicationResponse get(@AuthenticationPrincipal DatabaseUserPrincipal p,@PathVariable Long applicationId){return service.get(p.getId(),applicationId);}
}
