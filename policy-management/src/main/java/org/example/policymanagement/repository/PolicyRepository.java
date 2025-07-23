package org.example.policymanagement.repository;

import org.example.policymanagement.model.Policy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PolicyRepository {
    private final JdbcTemplate jdbcTemplate;
    
    private static final String INSERT_POLICY = 
        "INSERT INTO policies (id, business_name, coverage_period, coverage_amount) VALUES (?, ?, ?, ?)";
    private static final String SELECT_POLICY_BY_ID = 
        "SELECT id, business_name, coverage_period, coverage_amount FROM policies WHERE id = ?";
    private static final String UPDATE_POLICY = 
        "UPDATE policies SET business_name = ?, coverage_period = ?, coverage_amount = ? WHERE id = ?";
    private static final String DELETE_POLICY = 
        "DELETE FROM policies WHERE id = ?";
    private static final String SELECT_ALL_POLICIES = 
        "SELECT id, business_name, coverage_period, coverage_amount FROM policies";

    private final RowMapper<Policy> policyMapper = (rs, rowNum) -> 
        new Policy(
            UUID.fromString(rs.getString("id")),
            rs.getString("business_name"),
            rs.getDate("coverage_period").toLocalDate(),
            rs.getDouble("coverage_amount")
        );

    public PolicyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Policy save(Policy policy) {
        if (findById(policy.getId()).isPresent()) {
            jdbcTemplate.update(UPDATE_POLICY,
                policy.getBusinessName(),
                policy.getCoveragePeriod(),
                policy.getCoverageAmount(),
                policy.getId()
            );
        } else {
            jdbcTemplate.update(INSERT_POLICY,
                policy.getId(),
                policy.getBusinessName(),
                policy.getCoveragePeriod(),
                policy.getCoverageAmount()
            );
        }
        return policy;
    }

    public Optional<Policy> findById(UUID id) {
        List<Policy> results = jdbcTemplate.query(
            SELECT_POLICY_BY_ID,
            policyMapper,
            id
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<Policy> findAll() {
        return jdbcTemplate.query(SELECT_ALL_POLICIES, policyMapper);
    }

    public void deleteById(UUID id) {
        jdbcTemplate.update(DELETE_POLICY, id);
    }

    public List<Policy> findByIds(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        String inSql = String.join(",", java.util.Collections.nCopies(ids.size(), "?"));
        String sql = "SELECT id, business_name, coverage_period, coverage_amount FROM policies WHERE id IN (" + inSql + ")";
        return jdbcTemplate.query(sql, policyMapper, ids.toArray());
    }
} 