package com.amprima.tech.controller;

import com.amprima.tech.dto.LeadDTO;
import com.amprima.tech.dto.LeadResponseDTO;
import com.amprima.tech.service.LeadService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "https://amprima.com", "https://www.amprima.com"})
public class LeadController {

    private final LeadService leadService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createLead(
            @Valid @RequestBody LeadDTO leadDTO,
            HttpServletRequest request) {

        log.info("Received lead submission from: {}", leadDTO.getEmail());

        try {
            LeadResponseDTO createdLead = leadService.createLead(leadDTO, request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Thank you! Your inquiry has been received. We'll contact you within 24 hours.");
            response.put("leadId", createdLead.getId());
            response.put("data", createdLead);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            log.error("Error creating lead", e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "An error occurred while submitting your inquiry. Please try again or contact us directly.");
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<List<LeadResponseDTO>> getAllLeads(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer recentDays) {

        log.info("Fetching leads - status: {}, recentDays: {}", status, recentDays);

        List<LeadResponseDTO> leads;

        if (status != null && !status.isEmpty()) {
            leads = leadService.getLeadsByStatus(status);
        } else if (recentDays != null && recentDays > 0) {
            leads = leadService.getRecentLeads(recentDays);
        } else {
            leads = leadService.getAllLeads();
        }

        return ResponseEntity.ok(leads);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeadResponseDTO> getLeadById(@PathVariable Long id) {
        log.info("Fetching lead with ID: {}", id);
        LeadResponseDTO lead = leadService.getLeadById(id);
        return ResponseEntity.ok(lead);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<LeadResponseDTO> updateLeadStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {

        String newStatus = statusUpdate.get("status");
        log.info("Updating lead {} status to: {}", id, newStatus);

        LeadResponseDTO updatedLead = leadService.updateLeadStatus(id, newStatus);
        return ResponseEntity.ok(updatedLead);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "Amprima Tech Lead API");
        health.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(health);
    }
}