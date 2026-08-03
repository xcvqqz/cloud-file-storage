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

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUserWithRole(){
        buildTestRole();
       return buildTestUser();
    }

    private Role buildTestRole(){
        return Role.builder()
                .id(1L)
                .name(RoleName.USER)
                .build();
    }

    private User buildTestUser(){
       return User.builder()
                .id(1L)
                .name("testname")
                .password(passwordEncoder.encode("testdb"))
                .roles(setDefaultRole())
                .build();
    }

    private Set<Role> setDefaultRole() {

        Role role = roleRepository
                .findByName(RoleName.USER)
                .orElseThrow(()-> new RolesNotFoundException("Roles Not Found"));

        return new HashSet<Role>(Collections.singleton(role));

    }

    public RequestPostProcessor mockUserAs(String name, String... roles) {
        return user(name).password("password").roles(roles);
    }

}
