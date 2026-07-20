package io.github.xcvqqz.cloud_file_storage.controller;


import io.github.xcvqqz.cloud_file_storage.dto.response.UserAuthenticationResponse;
import io.github.xcvqqz.cloud_file_storage.mapper.AuthMapper;
import io.github.xcvqqz.cloud_file_storage.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class HomeController {

    private final AuthMapper authMapper;

    @GetMapping("/me")
    public ResponseEntity<UserAuthenticationResponse> getCurrentUser(@AuthenticationPrincipal UserDetailsImpl userDetails){
         return ResponseEntity.ok(authMapper.userNameToResponse(userDetails.getUsername()));
    }



}
