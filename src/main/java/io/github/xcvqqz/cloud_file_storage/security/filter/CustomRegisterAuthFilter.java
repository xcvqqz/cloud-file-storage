//package io.github.xcvqqz.cloud_file_storage.security.filter;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.github.xcvqqz.cloud_file_storage.dto.request.UserAuthenticationRequest;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import java.io.IOException;
//
//
//public class CustomRegisterAuthFilter extends BasicAuthenticationFilter {
//
//    private static final String REGISTER_URL = "/api/auth/sign-up";
//
//    public CustomRegisterAuthFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
//        super(authenticationManager, objectMapper);
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        if(!isLoginRequest(request, REGISTER_URL)){
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        UserAuthenticationRequest userAuthenticationRequest = parseToDTO(request);
//
//        UsernamePasswordAuthenticationToken authToken =
//                new UsernamePasswordAuthenticationToken(
//                        userAuthenticationRequest.name(),
//                        userAuthenticationRequest.password()
//                );
//
//        Authentication authentication = authenticationManager.authenticate(authToken);
//
//        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//        securityContext.setAuthentication(authentication);
//        SecurityContextHolder.setContext(securityContext);
//
//        httpSessionSecurityContextRepository.saveContext(securityContext, request, response);
//        filterChain.doFilter(request, response);
//
//    }
//
//
//}
