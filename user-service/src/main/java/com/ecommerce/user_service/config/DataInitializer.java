package com.ecommerce.user_service.config;

import com.ecommerce.user_service.model.entity.Role;
import com.ecommerce.user_service.model.entity.RoleName;
import com.ecommerce.user_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
    }

    private void initializeRoles() {
        Arrays.stream(RoleName.values()).forEach(roleName -> {
            if (!roleRepository.existsByName(roleName)) {
                Role role = new Role(null, roleName);
                roleRepository.save(role);
                System.out.println("Initialized role: " + roleName);
            }
        });
    }
}
