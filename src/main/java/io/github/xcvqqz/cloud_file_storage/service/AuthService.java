package io.github.xcvqqz.cloud_file_storage.service;

import io.github.xcvqqz.cloud_file_storage.dto.request.UserAuthenticationRequest;
import io.github.xcvqqz.cloud_file_storage.dto.request.UserRegistrationRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.UserAuthResponse;
import io.github.xcvqqz.cloud_file_storage.entity.Role;
import io.github.xcvqqz.cloud_file_storage.entity.RoleName;
import io.github.xcvqqz.cloud_file_storage.entity.User;
import io.github.xcvqqz.cloud_file_storage.mapper.UserMapper;
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

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public List<User> findAll(){
        return userRepository.findAll();
    }

    @Transactional(readOnly = false)
    public UserAuthResponse save(UserRegistrationRequest userRegistrationRequest){

        User newUser = User
                .builder()
                .name(userRegistrationRequest.name())
                .password(passwordEncoder.encode(userRegistrationRequest.password()))
                .roles(setDefaultRole())
                .build();

        try{
            userRepository.save(newUser);
        } catch (DataIntegrityViolationException e){
            System.out.println("Такой пользователь уже создан");
        }
        return userMapper.entityToAuthResponse(newUser);
    }


    public UserAuthResponse find(UserAuthenticationRequest userAuthenticationRequest){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userAuthenticationRequest.name(),
                        userAuthenticationRequest.password()
                ));

        User authenticatedUser = (User) authentication.getPrincipal();

        return userMapper.entityToAuthResponse(authenticatedUser);
    }

    private Set<Role> setDefaultRole() {
        return Set.of(Role
                .builder()
                .name(RoleName.USER)
                .build());
    }



}
