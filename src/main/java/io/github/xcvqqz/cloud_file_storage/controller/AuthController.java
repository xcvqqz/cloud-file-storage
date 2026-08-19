package io.github.xcvqqz.cloud_file_storage.controller;


import io.github.xcvqqz.cloud_file_storage.dto.request.UserAuthenticationRequest;
import io.github.xcvqqz.cloud_file_storage.dto.request.UserRegistrationRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.UserAuthenticationResponse;
import io.github.xcvqqz.cloud_file_storage.service.auth.AuthenticationService;
import io.github.xcvqqz.cloud_file_storage.service.auth.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authService;
    private final UserService userService;

    @PostMapping("/sign-in")
    public ResponseEntity<UserAuthenticationResponse> signIn(@Valid @RequestBody UserAuthenticationRequest userAuthenticationRequest, HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.authenticateUser(userAuthenticationRequest, request, response));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserAuthenticationResponse> signUp(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest, HttpServletRequest request, HttpServletResponse response) {

        userService.register(userRegistrationRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        authService.authenticateUser(
                                new UserAuthenticationRequest(
                                        userRegistrationRequest.name(),
                                        userRegistrationRequest.password()
                                ),
                                request,
                                response
                        )
                );
    }



//      return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(userRegistrationRequest));

}