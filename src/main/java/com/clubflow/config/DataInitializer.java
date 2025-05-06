
package com.clubflow.config;

import com.clubflow.model.Role;
import com.clubflow.model.User;
import com.clubflow.repository.RoleRepository;
import com.clubflow.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create roles if they don't exist
            if (roleRepository.count() == 0) {
                Role adminRole = new Role();
                adminRole.setName(Role.ERole.ROLE_ADMIN);
                roleRepository.save(adminRole);

                Role userRole = new Role();
                userRole.setName(Role.ERole.ROLE_USER);
                roleRepository.save(userRole);

                logger.info("Roles initialized");
            }

            // Create admin user if no users exist
            if (userRepository.count() == 0) {
                User adminUser = new User();
                adminUser.setFirstName("Admin");
                adminUser.setLastName("User");
                adminUser.setEmail("admin@clubflow.com");
                adminUser.setPassword(passwordEncoder.encode("admin123"));

                Set<Role> roles = new HashSet<>();
                Role adminRole = roleRepository.findByName(Role.ERole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(adminRole);
                adminUser.setRoles(roles);

                userRepository.save(adminUser);
                logger.info("Admin user created with email: admin@clubflow.com and password: admin123");
            }
        };
    }
}