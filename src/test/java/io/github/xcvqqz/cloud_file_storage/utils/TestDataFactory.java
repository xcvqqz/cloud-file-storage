package io.github.xcvqqz.cloud_file_storage.utils;


import io.github.xcvqqz.cloud_file_storage.entity.Role;
import io.github.xcvqqz.cloud_file_storage.entity.RoleName;
import io.github.xcvqqz.cloud_file_storage.entity.User;
import io.github.xcvqqz.cloud_file_storage.exception.RolesNotFoundException;
import io.github.xcvqqz.cloud_file_storage.repository.RoleRepository;
import io.github.xcvqqz.cloud_file_storage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@Component
@RequiredArgsConstructor
public class TestDataFactory {

    private static final String TEST_USER_NAME = "testname";
    private static final String TEST_USER_PASSWORD = "testpassword";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUserWithRole(){
        findOrCreateUserRole();
        return buildTestUser();
    }

    private User buildTestUser(){
       User user = User.builder()
                .name(TEST_USER_NAME)
                .password(passwordEncoder.encode(TEST_USER_PASSWORD))
                .roles(new HashSet<>(Collections.singleton(findOrCreateUserRole())))
                .build();
       return userRepository.save(user);
    }

    private Role findOrCreateUserRole() {

        return roleRepository
                .findByName(RoleName.USER).orElseGet(() -> {
                    Role newRole = Role.builder()
                            .name(RoleName.USER)
                            .build();
                  return roleRepository.save(newRole);
                });
    }

   public void clearAll(){
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    public RequestPostProcessor mockUserAs(String name, String... roles) {
        return user(name).password("password").roles(roles);
    }

    public String getTestUsername() {
        return TEST_USER_NAME;
    }

}
