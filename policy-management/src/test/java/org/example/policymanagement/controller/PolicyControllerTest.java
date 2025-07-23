package org.example.policymanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.policymanagement.dto.CreatePolicyRequest;
import org.example.policymanagement.dto.UpdatePolicyRequest;
import org.example.policymanagement.model.Policy;
import org.example.policymanagement.service.PolicyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PolicyController.class)
class PolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PolicyService policyService;

    @Test
    void shouldCreatePolicy() throws Exception {
        CreatePolicyRequest request = new CreatePolicyRequest(
            "Acme Corp",
            LocalDate.of(2024, 12, 31),
            100000.00
        );

        Policy policy = new Policy("Acme Corp", LocalDate.of(2024, 12, 31), 100000.00);
        when(policyService.createPolicy(
            eq("Acme Corp"),
            eq(LocalDate.of(2024, 12, 31)),
            eq(100000.00)
        )).thenReturn(policy);

        mockMvc.perform(post("/api/policies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.businessName").value("Acme Corp"));
    }

    @Test
    void shouldGetPolicy() throws Exception {
        UUID id = UUID.randomUUID();
        Policy policy = new Policy(id, "Acme Corp", LocalDate.of(2024, 12, 31), 100000.00);
        when(policyService.getPolicy(id)).thenReturn(Optional.of(policy));

        mockMvc.perform(get("/api/policies/" + id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(jsonPath("$.businessName").value("Acme Corp"));
    }

    @Test
    void shouldReturn404WhenPolicyNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(policyService.getPolicy(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/policies/" + id))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdatePolicy() throws Exception {
        UUID id = UUID.randomUUID();
        UpdatePolicyRequest request = new UpdatePolicyRequest(
            "Acme Corp Updated",
            null,
            150000.00
        );

        Policy updatedPolicy = new Policy(id, "Acme Corp Updated", LocalDate.of(2024, 12, 31), 150000.00);
        when(policyService.updatePolicy(eq(id), any(), any(), any())).thenReturn(Optional.of(updatedPolicy));

        mockMvc.perform(put("/api/policies/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.businessName").value("Acme Corp Updated"));
    }

    @Test
    void shouldGetAllPolicies() throws Exception {
        List<Policy> policies = List.of(
            new Policy(UUID.randomUUID(), "Acme Corp", LocalDate.of(2024, 12, 31), 100000.00),
            new Policy(UUID.randomUUID(), "Globex Corp", LocalDate.of(2024, 12, 31), 200000.00)
        );
        when(policyService.getAllPolicies()).thenReturn(policies);

        mockMvc.perform(get("/api/policies"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldDeletePolicy() throws Exception {
        UUID id = UUID.randomUUID();
        when(policyService.deletePolicy(id)).thenReturn(true);

        mockMvc.perform(delete("/api/policies/" + id))
            .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentPolicy() throws Exception {
        UUID id = UUID.randomUUID();
        when(policyService.deletePolicy(id)).thenReturn(false);

        mockMvc.perform(delete("/api/policies/" + id))
            .andExpect(status().isNotFound());
    }
} 