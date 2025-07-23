package org.example.policymanagement.repository;

import org.example.policymanagement.TestcontainersConfiguration;
import org.example.policymanagement.model.Policy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
class PolicyRepositoryTest {

    @Autowired
    private PolicyRepository policyRepository;

    @Test
    void shouldPerformCRUDOperations() {
        // Create
        Policy policy = new Policy(
            "Acme Corp",
            LocalDate.of(2024, 12, 31),
            100000.00
        );
        
        Policy savedPolicy = policyRepository.save(policy);
        assertThat(savedPolicy.getId()).isNotNull();
        assertThat(savedPolicy.getBusinessName()).isEqualTo("Acme Corp");
        assertThat(savedPolicy.getCoveragePeriod()).isEqualTo(LocalDate.of(2024, 12, 31));
        assertThat(savedPolicy.getCoverageAmount()).isEqualTo(100000.00);

        // Read
        Optional<Policy> foundPolicy = policyRepository.findById(savedPolicy.getId());
        assertThat(foundPolicy).isPresent();
        assertThat(foundPolicy.get().getBusinessName()).isEqualTo("Acme Corp");

        // Update
        Policy policyToUpdate = foundPolicy.get();
        policyToUpdate.setBusinessName("Acme Corporation");
        policyToUpdate.setCoverageAmount(150000.00);
        Policy updatedPolicy = policyRepository.save(policyToUpdate);
        assertThat(updatedPolicy.getBusinessName()).isEqualTo("Acme Corporation");
        assertThat(updatedPolicy.getCoverageAmount()).isEqualTo(150000.00);

        // Delete
        policyRepository.deleteById(updatedPolicy.getId());
        Optional<Policy> deletedPolicy = policyRepository.findById(updatedPolicy.getId());
        assertThat(deletedPolicy).isEmpty();
    }
} 