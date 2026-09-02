package io.github.xcvqqz.cloud_file_storage.service.auth;


import io.github.xcvqqz.cloud_file_storage.dto.request.UserAuthenticationRequest;
import io.github.xcvqqz.cloud_file_storage.dto.request.UserRegistrationRequest;
import io.github.xcvqqz.cloud_file_storage.entity.Role;
import io.github.xcvqqz.cloud_file_storage.entity.RoleName;
import io.github.xcvqqz.cloud_file_storage.entity.User;
import io.github.xcvqqz.cloud_file_storage.exception.PasswordMismatchException;
import io.github.xcvqqz.cloud_file_storage.exception.RolesNotFoundException;
import io.github.xcvqqz.cloud_file_storage.mapper.AuthMapper;
import io.github.xcvqqz.cloud_file_storage.repository.RoleRepository;
import io.github.xcvqqz.cloud_file_storage.repository.UserRepository;
import io.github.xcvqqz.cloud_file_storage.security.UserDetailsImpl;
import io.github.xcvqqz.cloud_file_storage.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private static final String PASSWORD_MISS_MATCH_ERROR = "password and confirm password dont match";;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;

    @Transactional(readOnly = false)
    public UserAuthenticationRequest register(UserRegistrationRequest userRegistrationRequest) {

        if(!userRegistrationRequest.password().equals(userRegistrationRequest.confirmPassword())){
            throw new PasswordMismatchException(PASSWORD_MISS_MATCH_ERROR);
        }

        User newUser = User
                .builder()
                .name(userRegistrationRequest.name())
                .password(passwordEncoder.encode(userRegistrationRequest.password()))
                .roles(setDefaultRole())
                .build();

       return  authMapper.userToAuthenticationRequest(userRepository.save(newUser));
    }


    public Long getCurrentUserId(){

        Long userId;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && auth.isAuthenticated()){
            UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
            userId = userDetails.getId();
            log.info("для данного пользователя получен id: {}", userId);
        }
        throw new UsernameNotFoundException("пользователь не найден, либо не авторизован");
    }

    private Set<Role> setDefaultRole() {

        Role role = roleRepository
                .findByName(RoleName.USER)
                .orElseThrow(()-> new RolesNotFoundException("Roles Not Found"));

        return new HashSet<Role>(Collections.singleton(role));

    }



}









//    public UserAuthResponse login(UserAuthenticationRequest userAuthenticationRequest, HttpServletRequest httpRequest) {
//
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        userAuthenticationRequest.name(),
//                        userAuthenticationRequest.password()
//                ));
//
//        return authMapper.authenticationToResponse(authentication);
//    }