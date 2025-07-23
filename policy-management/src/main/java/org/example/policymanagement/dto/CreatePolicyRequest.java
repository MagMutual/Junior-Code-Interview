package org.example.policymanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record CreatePolicyRequest(
    @NotBlank(message = "Business name is required")
    String businessName,

    @NotNull(message = "Coverage period is required")
    LocalDate coveragePeriod,

    @Positive(message = "Coverage amount must be greater than zero")
    double coverageAmount
) {} 