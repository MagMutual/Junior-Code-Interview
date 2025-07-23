package org.example.policymanagement;

import org.springframework.boot.SpringApplication;

public class TestPolicyManagementApplication {

    public static void main(String[] args) {
        SpringApplication.from(PolicyManagementApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
