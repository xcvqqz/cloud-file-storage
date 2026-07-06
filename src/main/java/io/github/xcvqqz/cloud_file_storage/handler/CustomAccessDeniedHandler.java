package io.github.xcvqqz.cloud_file_storage.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xcvqqz.cloud_file_storage.dto.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

import static io.github.xcvqqz.cloud_file_storage.handler.CustomAuthenticationEntryPoint.CHARACTER_ENCODING_PARAM;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    protected final static String FORBIDDEN_ERROR_MESSAGE = "You are not authorized to access this profile";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(CHARACTER_ENCODING_PARAM);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.name(),
                FORBIDDEN_ERROR_MESSAGE,
                request.getRequestURI(),
                LocalDateTime.now()
        );

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), errorResponse);

    }
}
