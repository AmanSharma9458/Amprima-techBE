package com.amprima.tech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadResponseDTO {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String companyName;
    private String serviceInterest;
    private String message;
    private String source;
    private String status;
    private LocalDateTime createdAt;
}