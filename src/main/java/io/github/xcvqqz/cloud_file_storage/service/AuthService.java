package io.github.xcvqqz.cloud_file_storage.service;

import io.github.xcvqqz.cloud_file_storage.dto.request.UserAuthenticationRequest;
import io.github.xcvqqz.cloud_file_storage.dto.request.UserRegistrationRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.UserAuthResponse;
import io.github.xcvqqz.cloud_file_storage.entity.Role;
import io.github.xcvqqz.cloud_file_storage.entity.RoleName;
import io.github.xcvqqz.cloud_file_storage.entity.User;
import io.github.xcvqqz.cloud_file_storage.exception.DataBaseException;
import io.github.xcvqqz.cloud_file_storage.exception.PasswordMismatchException;
import io.github.xcvqqz.cloud_file_storage.exception.RolesNotFoundException;
import io.github.xcvqqz.cloud_file_storage.exception.UserAlreadyExistsException;
import io.github.xcvqqz.cloud_file_storage.mapper.AuthMapper;
import io.github.xcvqqz.cloud_file_storage.repository.RoleRepository;
import io.github.xcvqqz.cloud_file_storage.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.exception.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private static final String PASSWORD_MISS_MATCH_ERROR = "password and confirm password dont match";
    private static final String USER_ALREADY_EXIST_MESSAGE = "A user with this name is already exist";
    private static final String DATABASE_ERROR_MESSAGE = "Database error: %s";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final HttpSessionSecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();


    public List<UserAuthResponse> findAll() {
        List<UserAuthResponse> responses = new ArrayList<>();
        List<User> users = userRepository.findAll();

        for(User user : users){
            responses.add(authMapper.entityToResponse(user));
        }
        return responses;
    }

    @Transactional(readOnly = false)
    public UserAuthResponse register(UserRegistrationRequest userRegistrationRequest) {

        if(!userRegistrationRequest.password().equals(userRegistrationRequest.confirmPassword())){
            throw new PasswordMismatchException(PASSWORD_MISS_MATCH_ERROR);
        }

        User newUser = User
                .builder()
                .name(userRegistrationRequest.name())
                .password(passwordEncoder.encode(userRegistrationRequest.password()))
                .roles(setDefaultRole())
                .build();

        userRepository.save(newUser);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRegistrationRequest.name(),
                        userRegistrationRequest.password()
                ));

        return authMapper.authenticationToResponse(authentication);

    }



    public UserAuthResponse login(UserAuthenticationRequest userAuthenticationRequest, HttpServletRequest httpRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userAuthenticationRequest.name(),
                        userAuthenticationRequest.password()
                ));


//        HttpSession session = httpRequest.getSession(true);
//        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
//        httpRequest.changeSessionId();

//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        context.setAuthentication(authentication);
//        SecurityContextHolder.setContext(context);

        // Сохраняем контекст в сессию через репозиторий
//        securityContextRepository.saveContext(context, httpRequest, httpResponse);

        return authMapper.authenticationToResponse(authentication);
    }


    private Set<Role> setDefaultRole() {

        Role role = roleRepository
                .findByName(RoleName.USER)
                .orElseThrow(()-> new RolesNotFoundException("Roles Not Found"));

        return new HashSet<Role>(Collections.singleton(role));

    }



}
