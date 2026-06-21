package io.github.xcvqqz.cloud_file_storage.controller;


import io.github.xcvqqz.cloud_file_storage.dto.request.UserAuthenticationRequest;
import io.github.xcvqqz.cloud_file_storage.dto.request.UserRegistrationRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.UserAuthResponse;
import io.github.xcvqqz.cloud_file_storage.service.UserDetailsServiceImpl;
import io.github.xcvqqz.cloud_file_storage.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private UserDetailsServiceImpl userDetailsService;
    private UserService userService;

    @GetMapping("/sign-in")
    public ResponseEntity<UserAuthResponse> signIn(@Valid @RequestBody UserAuthenticationRequest userAuthenticationRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.find(userAuthenticationRequest));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserAuthResponse> signUp(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userRegistrationRequest));
    }

//    @GetMapping("/sign-out")
//    public String logout(@ModelAttribute("user") UserRegistrationDTO userRegistration) {
//        return SIGN_UP_VIEW;
//    }



}
