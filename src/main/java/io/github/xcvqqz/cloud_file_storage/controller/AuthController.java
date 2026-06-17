package io.github.xcvqqz.cloud_file_storage.controller;


import io.github.xcvqqz.cloud_file_storage.service.UserDetailsServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private UserDetailsServiceImpl userDetailsService;



    @GetMapping("/sign-in")
    public String showSignIn(@ModelAttribute("user") UserRegistrationDTO userRegistration) {
        return SIGN_UP_VIEW;
    }

    @GetMapping("/sign-up")
    public String showSignUp(@ModelAttribute("user") UserRegistrationDTO userRegistration) {
        return SIGN_UP_VIEW;
    }

    @GetMapping("/sign-out")
    public String logout(@ModelAttribute("user") UserRegistrationDTO userRegistration) {
        return SIGN_UP_VIEW;
    }



}
