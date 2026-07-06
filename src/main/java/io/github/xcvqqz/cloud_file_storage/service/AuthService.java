package io.github.xcvqqz.cloud_file_storage.service;

import io.github.xcvqqz.cloud_file_storage.dto.request.UserAuthenticationRequest;
import io.github.xcvqqz.cloud_file_storage.dto.request.UserRegistrationRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.UserAuthResponse;
import io.github.xcvqqz.cloud_file_storage.entity.Role;
import io.github.xcvqqz.cloud_file_storage.entity.RoleName;
import io.github.xcvqqz.cloud_file_storage.entity.User;
import io.github.xcvqqz.cloud_file_storage.exception.RolesNotFoundException;
import io.github.xcvqqz.cloud_file_storage.mapper.AuthMapper;
import io.github.xcvqqz.cloud_file_storage.repository.RoleRepository;
import io.github.xcvqqz.cloud_file_storage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public List<UserAuthResponse> findAll() {
        List<UserAuthResponse> responses = new ArrayList<>();
        List<User> users = userRepository.findAll();

        for(User user : users){
            responses.add(authMapper.entityToResponse(user));
        }

        return responses;

    }

    @Transactional(readOnly = false)
    public UserAuthResponse save(UserRegistrationRequest userRegistrationRequest) {

        User newUser = User
                .builder()
                .name(userRegistrationRequest.name())
                .password(passwordEncoder.encode(userRegistrationRequest.password()))
                .roles(setDefaultRole())
                .build();

        try {
            userRepository.save(newUser);
        } catch (DataIntegrityViolationException e) {
            System.out.println("Такой пользователь уже создан");
        }
        return authMapper.entityToResponse(newUser);
    }


    public UserAuthResponse find(UserAuthenticationRequest userAuthenticationRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userAuthenticationRequest.name(),
                        userAuthenticationRequest.password()
                ));

        return authMapper.authenticationToResponse(authentication);
    }


    private Set<Role> setDefaultRole() {

        Role role = roleRepository
                .findByName(RoleName.USER)
                .orElseThrow(()-> new RolesNotFoundException("Roles Not Found"));

        return new HashSet<Role>(Collections.singleton(role));

    }



}
