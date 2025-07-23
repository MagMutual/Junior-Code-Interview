package org.example.policymanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.policymanagement.dto.CreatePolicyRequest;
import org.example.policymanagement.dto.UpdatePolicyRequest;
import org.example.policymanagement.model.Policy;
import org.example.policymanagement.service.PolicyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
@Tag(name = "Policy Management", description = "APIs for managing insurance policies")
public class PolicyController {

    private final PolicyService policyService;

    @PostMapping
    @Operation(summary = "Create a new policy")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Policy created"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<Policy> createPolicy(@Valid @RequestBody CreatePolicyRequest request) {
        Policy policy = policyService.createPolicy(
            request.businessName(),
            request.coveragePeriod(),
            request.coverageAmount()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(policy);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a policy by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Policy found"),
        @ApiResponse(responseCode = "404", description = "Policy not found")
    })
    public ResponseEntity<Policy> getPolicy(
        @Parameter(description = "Policy ID") @PathVariable UUID id
    ) {
        return policyService.getPolicy(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing policy")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Policy updated"),
        @ApiResponse(responseCode = "404", description = "Policy not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<Policy> updatePolicy(
        @Parameter(description = "Policy ID") @PathVariable UUID id,
        @Valid @RequestBody UpdatePolicyRequest request
    ) {
        return policyService.updatePolicy(
                id,
                request.businessName(),
                request.coveragePeriod(),
                request.coverageAmount()
            )
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all policies")
    @ApiResponse(responseCode = "200", description = "List of policies")
    public List<Policy> getAllPolicies() {
        return policyService.getAllPolicies();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a policy")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Policy deleted"),
        @ApiResponse(responseCode = "404", description = "Policy not found")
    })
    public ResponseEntity<Void> deletePolicy(
        @Parameter(description = "Policy ID") @PathVariable UUID id
    ) {
        return policyService.deletePolicy(id)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }
} 