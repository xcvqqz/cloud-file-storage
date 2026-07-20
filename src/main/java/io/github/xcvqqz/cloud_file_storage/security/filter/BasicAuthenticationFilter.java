package io.github.xcvqqz.cloud_file_storage.security.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xcvqqz.cloud_file_storage.dto.request.UserAuthenticationRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@RequiredArgsConstructor
public class BasicAuthenticationFilter extends OncePerRequestFilter {

    protected final AuthenticationManager authenticationManager;
    protected final ObjectMapper objectMapper;
    protected final HttpSessionSecurityContextRepository httpSessionSecurityContextRepository
            = new HttpSessionSecurityContextRepository();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(request, response);
    }

    protected UserAuthenticationRequest parseToDTO(HttpServletRequest httpServletRequest){
        try{
            return objectMapper.readValue(httpServletRequest.getInputStream(), UserAuthenticationRequest.class);
        } catch (IOException ex){
            throw new AuthenticationServiceException("cant read body request: " + ex.getMessage());
        }
    }

    protected boolean isLoginRequest(HttpServletRequest request, String path) {
        return HttpMethod.POST.matches(request.getMethod())
                && path.equals(request.getServletPath());
    }


}