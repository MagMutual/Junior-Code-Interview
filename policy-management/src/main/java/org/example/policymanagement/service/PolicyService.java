package org.example.policymanagement.service;

import lombok.RequiredArgsConstructor;
import org.example.policymanagement.exception.PolicyValidationException;
import org.example.policymanagement.model.Policy;
import org.example.policymanagement.repository.PolicyRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;

    public Policy createPolicy(String businessName, LocalDate coveragePeriod, double coverageAmount) {
        validateCreatePolicy(businessName, coveragePeriod, coverageAmount);
        Policy policy = new Policy(businessName, coveragePeriod, coverageAmount);
        return policyRepository.save(policy);
    }

    public Optional<Policy> getPolicy(UUID id) {
        if (id == null) {
            throw new PolicyValidationException("Policy ID cannot be null");
        }
        return policyRepository.findById(id);
    }

    public Optional<Policy> updatePolicy(UUID id, String businessName, LocalDate coveragePeriod, Double coverageAmount) {
        validateUpdatePolicy(id, businessName, coveragePeriod, coverageAmount);
        return policyRepository.findById(id)
            .map(policy -> {
                if (businessName != null) policy.setBusinessName(businessName);
                if (coveragePeriod != null) policy.setCoveragePeriod(coveragePeriod);
                if (coverageAmount != null) policy.setCoverageAmount(coverageAmount);
                return policyRepository.save(policy);
            });
    }

    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }

    public boolean deletePolicy(UUID id) {
        if (id == null) {
            throw new PolicyValidationException("Policy ID cannot be null");
        }
        return policyRepository.findById(id)
            .map(policy -> {
                policyRepository.deleteById(id);
                return true;
            })
            .orElse(false);
    }

    public List<Policy> getPoliciesByIds(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new PolicyValidationException("Policy IDs list cannot be null or empty");
        }
        return policyRepository.findByIds(ids);
    }

    private void validateCreatePolicy(String businessName, LocalDate coveragePeriod, double coverageAmount) {
        if (!StringUtils.hasText(businessName)) {
            throw new PolicyValidationException("Business name is required");
        }
        if (coveragePeriod == null) {
            throw new PolicyValidationException("Coverage period is required");
        }
        if (coverageAmount <= 0) {
            throw new PolicyValidationException("Coverage amount must be greater than zero");
        }
    }

    private void validateUpdatePolicy(UUID id, String businessName, LocalDate coveragePeriod, Double coverageAmount) {
        if (id == null) {
            throw new PolicyValidationException("Policy ID cannot be null");
        }
        if (businessName == null && coveragePeriod == null && coverageAmount == null) {
            throw new PolicyValidationException("At least one field must be provided for update");
        }
        if (coverageAmount != null && coverageAmount <= 0) {
            throw new PolicyValidationException("Coverage amount must be greater than zero");
        }
    }
} 