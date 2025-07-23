package org.example.policymanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Policy {
    private UUID id;
    private String businessName;
    private LocalDate coveragePeriod;
    private double coverageAmount;

    public Policy(String businessName, LocalDate coveragePeriod, double coverageAmount) {
        this.id = UUID.randomUUID();
        this.businessName = businessName;
        this.coveragePeriod = coveragePeriod;
        this.coverageAmount = coverageAmount;
    }
} 