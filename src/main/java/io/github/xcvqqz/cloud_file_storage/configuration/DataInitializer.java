package io.github.xcvqqz.cloud_file_storage.configuration;


import io.github.xcvqqz.cloud_file_storage.entity.Role;
import io.github.xcvqqz.cloud_file_storage.entity.RoleName;
import io.github.xcvqqz.cloud_file_storage.entity.User;
import io.github.xcvqqz.cloud_file_storage.exception.RolesNotFoundException;
import io.github.xcvqqz.cloud_file_storage.repository.RoleRepository;
import io.github.xcvqqz.cloud_file_storage.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.name}")
    private String adminName;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Starting data initialization...");

        Role userRole = createRoleIfNotExist(RoleName.USER);
        Role adminRole = createRoleIfNotExist(RoleName.ADMIN);
        log.info("Roles initialized: [{}], [{}]", userRole.getName(), adminRole.getName());

        createAdminUserIfNotExist(Set.of(userRole, adminRole));
        log.info("Admin user initialization complete.");
    }

    private Role createRoleIfNotExist(RoleName roleName) {
        return roleRepository
                .findByName(roleName)
                .orElseGet(() -> {
                    log.info("Creating role: {}", roleName);
                    Role newRole = Role.builder()
                            .name(roleName)
                            .build();
                    return roleRepository.save(newRole);
                });
    }

    private User createAdminUserIfNotExist(Set<Role> roles) {

        return userRepository
                .findByName(adminName)
                .orElseGet(() -> {
                    User newUser = User
                            .builder()
                            .name(adminName)
                            .password(passwordEncoder.encode(adminPassword))
                            .roles(new HashSet<>(roles))
                            .build();

                    return userRepository.save(newUser);

                });
    }


}