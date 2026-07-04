package io.github.xcvqqz.cloud_file_storage.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xcvqqz.cloud_file_storage.dto.response.ErrorResponse;
import io.github.xcvqqz.cloud_file_storage.exception.RolesNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private static final String ERROR_VIEW = "error";
    private static final String HOME_REDIRECT = "redirect:/home";
    private static final String SIGN_UP_REDIRECT = "redirect:/sign-up";
    private static final String SIGN_IN_REDIRECT = "redirect:/sign-in";
    private static final String GLOBAL_ERROR_ATTR = "global_error";
    

    @ExceptionHandler({RolesNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(Exception ex,  HttpServletRequest request) {

        log.warn("Entity not found: URI={}, type={}, msg={}",
                request.getRequestURI(), ex.getClass().getSimpleName(), ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse
                (404, "Not Found", "Пользователь не найден", "путь", LocalDateTime.now());

        return ResponseEntity.status(404).body(errorResponse);
    }


}
