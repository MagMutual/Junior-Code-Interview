package org.example.policymanagement.dto;

import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record UpdatePolicyRequest(
    String businessName,
    LocalDate coveragePeriod,
    
    @Positive(message = "Coverage amount must be greater than zero")
    Double coverageAmount
) {} 