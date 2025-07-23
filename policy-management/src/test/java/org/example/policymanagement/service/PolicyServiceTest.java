package org.example.policymanagement.service;

import org.example.policymanagement.model.Policy;
import org.example.policymanagement.repository.PolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.example.policymanagement.exception.PolicyValidationException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PolicyServiceTest {

    @Mock
    private PolicyRepository policyRepository;

    private PolicyService policyService;

    @BeforeEach
    void setUp() {
        policyService = new PolicyService(policyRepository);
    }

    @Test
    void shouldCreateNewPolicy() {
        // Given
        Policy newPolicy = new Policy("Acme Corp", LocalDate.of(2024, 12, 31), 100000.00);
        when(policyRepository.save(any(Policy.class))).thenReturn(newPolicy);

        // When
        Policy created = policyService.createPolicy("Acme Corp", LocalDate.of(2024, 12, 31), 100000.00);

        // Then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getBusinessName()).isEqualTo("Acme Corp");
        verify(policyRepository).save(any(Policy.class));
    }

    @Test
    void shouldRetrieveExistingPolicy() {
        // Given
        UUID id = UUID.randomUUID();
        Policy existingPolicy = new Policy(id, "Acme Corp", LocalDate.of(2024, 12, 31), 100000.00);
        when(policyRepository.findById(id)).thenReturn(Optional.of(existingPolicy));

        // When
        Optional<Policy> found = policyService.getPolicy(id);

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getBusinessName()).isEqualTo("Acme Corp");
        verify(policyRepository).findById(id);
    }

    @Test
    void shouldUpdateExistingPolicy() {
        // Given
        UUID id = UUID.randomUUID();
        Policy existingPolicy = new Policy(id, "Acme Corp", LocalDate.of(2024, 12, 31), 100000.00);
        Policy updatedPolicy = new Policy(id, "Acme Corp Updated", LocalDate.of(2024, 12, 31), 150000.00);
        
        when(policyRepository.findById(id)).thenReturn(Optional.of(existingPolicy));
        when(policyRepository.save(any(Policy.class))).thenReturn(updatedPolicy);

        // When
        Optional<Policy> result = policyService.updatePolicy(id, "Acme Corp Updated", null, 150000.00);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getBusinessName()).isEqualTo("Acme Corp Updated");
        assertThat(result.get().getCoverageAmount()).isEqualTo(150000.00);
        verify(policyRepository).save(any(Policy.class));
    }

    @Test
    void shouldReturnAllPolicies() {
        // Given
        List<Policy> policies = List.of(
            new Policy(UUID.randomUUID(), "Acme Corp", LocalDate.of(2024, 12, 31), 100000.00),
            new Policy(UUID.randomUUID(), "Globex Corp", LocalDate.of(2024, 12, 31), 200000.00)
        );
        when(policyRepository.findAll()).thenReturn(policies);

        // When
        List<Policy> result = policyService.getAllPolicies();

        // Then
        assertThat(result).hasSize(2);
        verify(policyRepository).findAll();
    }

    @Test
    void shouldDeleteExistingPolicy() {
        // Given
        UUID id = UUID.randomUUID();
        when(policyRepository.findById(id)).thenReturn(Optional.of(new Policy()));
        doNothing().when(policyRepository).deleteById(id);

        // When
        boolean result = policyService.deletePolicy(id);

        // Then
        assertThat(result).isTrue();
        verify(policyRepository).deleteById(id);
    }

    @Test
    void shouldThrowExceptionWhenCreatingPolicyWithNullBusinessName() {
        PolicyValidationException exception = assertThrows(
            PolicyValidationException.class,
            () -> policyService.createPolicy(null, LocalDate.now(), 100000.00)
        );
        assertThat(exception.getMessage()).isEqualTo("Business name is required");
    }

    @Test
    void shouldThrowExceptionWhenCreatingPolicyWithEmptyBusinessName() {
        PolicyValidationException exception = assertThrows(
            PolicyValidationException.class,
            () -> policyService.createPolicy("", LocalDate.now(), 100000.00)
        );
        assertThat(exception.getMessage()).isEqualTo("Business name is required");
    }

    @Test
    void shouldThrowExceptionWhenCreatingPolicyWithNullCoveragePeriod() {
        PolicyValidationException exception = assertThrows(
            PolicyValidationException.class,
            () -> policyService.createPolicy("Acme Corp", null, 100000.00)
        );
        assertThat(exception.getMessage()).isEqualTo("Coverage period is required");
    }

    @Test
    void shouldThrowExceptionWhenCreatingPolicyWithZeroCoverageAmount() {
        PolicyValidationException exception = assertThrows(
            PolicyValidationException.class,
            () -> policyService.createPolicy("Acme Corp", LocalDate.now(), 0.0)
        );
        assertThat(exception.getMessage()).isEqualTo("Coverage amount must be greater than zero");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingPolicyWithNoChanges() {
        UUID id = UUID.randomUUID();
        PolicyValidationException exception = assertThrows(
            PolicyValidationException.class,
            () -> policyService.updatePolicy(id, null, null, null)
        );
        assertThat(exception.getMessage()).isEqualTo("At least one field must be provided for update");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingPolicyWithNullId() {
        PolicyValidationException exception = assertThrows(
            PolicyValidationException.class,
            () -> policyService.updatePolicy(null, "Acme Corp", null, null)
        );
        assertThat(exception.getMessage()).isEqualTo("Policy ID cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingPolicyWithInvalidCoverageAmount() {
        UUID id = UUID.randomUUID();
        PolicyValidationException exception = assertThrows(
            PolicyValidationException.class,
            () -> policyService.updatePolicy(id, null, null, -1.0)
        );
        assertThat(exception.getMessage()).isEqualTo("Coverage amount must be greater than zero");
    }

    @Test
    void shouldThrowExceptionWhenGettingPolicyWithNullId() {
        PolicyValidationException exception = assertThrows(
            PolicyValidationException.class,
            () -> policyService.getPolicy(null)
        );
        assertThat(exception.getMessage()).isEqualTo("Policy ID cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenDeletingPolicyWithNullId() {
        PolicyValidationException exception = assertThrows(
            PolicyValidationException.class,
            () -> policyService.deletePolicy(null)
        );
        assertThat(exception.getMessage()).isEqualTo("Policy ID cannot be null");
    }
} 