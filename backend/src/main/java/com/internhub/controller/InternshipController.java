package com.internhub.controller;

import com.internhub.dto.InternshipResponse;
import com.internhub.service.InternshipService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/internships")
public class InternshipController {
    private final InternshipService service;
    public InternshipController(InternshipService service) { this.service = service; }

    @GetMapping
    public Page<InternshipResponse> list(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(required = false) String q,
                                         @RequestParam(required = false) String location,
                                         @RequestParam(required = false) String workMode) {
        return service.listPublic(q, location, workMode, PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50), Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    @GetMapping("/{id}")
    public InternshipResponse get(@PathVariable Long id) { return service.getPublic(id); }
}
