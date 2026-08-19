package io.github.xcvqqz.cloud_file_storage.service.auth;

import io.github.xcvqqz.cloud_file_storage.dto.request.UserAuthenticationRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.UserAuthenticationResponse;
import io.github.xcvqqz.cloud_file_storage.mapper.AuthMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationService {

    private static final String PASSWORD_MISS_MATCH_ERROR = "password and confirm password dont match";
    private static final String USER_ALREADY_EXIST_MESSAGE = "A user with this name is already exist";
    private static final String DATABASE_ERROR_MESSAGE = "Database error: %s";

    private final AuthMapper authMapper;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;


    public UserAuthenticationResponse authenticateUser(UserAuthenticationRequest authenticationRequest, HttpServletRequest request, HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.name(),
                        authenticationRequest.password()
                ));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        securityContextRepository.saveContext(context, request, response);

        return authMapper.authenticationToResponse(authentication);
    }







}
