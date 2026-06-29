package io.github.xcvqqz.cloud_file_storage.controller;


import io.github.xcvqqz.cloud_file_storage.dto.request.UserAuthenticationRequest;
import io.github.xcvqqz.cloud_file_storage.dto.request.UserRegistrationRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.UserAuthResponse;
import io.github.xcvqqz.cloud_file_storage.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    @GetMapping("/sign-in")
    public ResponseEntity<UserAuthResponse> signIn(@Valid @RequestBody UserAuthenticationRequest userAuthenticationRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.find(userAuthenticationRequest));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserAuthResponse> signUp(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.save(userRegistrationRequest));
    }

//    @GetMapping("/sign-out")
//    public String logout(@ModelAttribute("user") UserRegistrationDTO userRegistration) {
//        return SIGN_UP_VIEW;
//    }



}