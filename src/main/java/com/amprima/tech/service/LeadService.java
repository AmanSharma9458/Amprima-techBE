package com.amprima.tech.service;

import com.amprima.tech.dto.LeadDTO;
import com.amprima.tech.dto.LeadResponseDTO;
import com.amprima.tech.entity.Lead;
import com.amprima.tech.repository.LeadRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeadService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LeadService.class);
    @Autowired
    private LeadRepository leadRepository;
//    private final EmailService emailService;

    @Transactional
    public LeadResponseDTO createLead(LeadDTO leadDTO, HttpServletRequest request) {
        log.info("Creating new lead for: {}", leadDTO.getEmail());

        Lead lead = new Lead();
        lead.setName(leadDTO.getName());
        lead.setPhone(leadDTO.getPhone());
        lead.setEmail(leadDTO.getEmail());
        lead.setCompanyName(leadDTO.getCompanyName());
        lead.setServiceInterest(leadDTO.getServiceInterest());
        lead.setMessage(leadDTO.getMessage());
        lead.setHearAboutUs(leadDTO.getHearAboutUs());
        lead.setSource(leadDTO.getSource() != null ? leadDTO.getSource() : "Website");
        lead.setIpAddress(getClientIp(request));
        lead.setStatus("NEW");

        Lead savedLead = leadRepository.save(lead);
        log.info("Lead saved with ID: {}", savedLead.getId());

        // Email notification code removed: not sending email anymore.

        return convertToResponseDTO(savedLead);
    }

    public List<LeadResponseDTO> getAllLeads() {
        return leadRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<LeadResponseDTO> getLeadsByStatus(String status) {
        return leadRepository.findByStatus(status).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<LeadResponseDTO> getRecentLeads(int days) {
        LocalDateTime sinceDate = LocalDateTime.now().minusDays(days);
        return leadRepository.findRecentLeads(sinceDate).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public LeadResponseDTO getLeadById(Long id) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lead not found with ID: " + id));
        return convertToResponseDTO(lead);
    }

    @Transactional
    public LeadResponseDTO updateLeadStatus(Long id, String status) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lead not found with ID: " + id));
        lead.setStatus(status);
        Lead updatedLead = leadRepository.save(lead);
        log.info("Lead {} status updated to: {}", id, status);
        return convertToResponseDTO(updatedLead);
    }

    private LeadResponseDTO convertToResponseDTO(Lead lead) {
        LeadResponseDTO dto = new LeadResponseDTO();
        dto.setId(lead.getId());
        dto.setName(lead.getName());
        dto.setPhone(lead.getPhone());
        dto.setEmail(lead.getEmail());
        dto.setCompanyName(lead.getCompanyName());
        dto.setServiceInterest(lead.getServiceInterest());
        dto.setMessage(lead.getMessage());
        dto.setSource(lead.getSource());
        dto.setStatus(lead.getStatus());
        dto.setCreatedAt(lead.getCreatedAt());
        return dto;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}