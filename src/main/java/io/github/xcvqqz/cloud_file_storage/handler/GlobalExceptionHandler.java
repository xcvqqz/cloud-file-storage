package io.github.xcvqqz.cloud_file_storage.handler;

import io.github.xcvqqz.cloud_file_storage.dto.response.ErrorResponse;
import io.github.xcvqqz.cloud_file_storage.exception.DataBaseException;
import io.github.xcvqqz.cloud_file_storage.exception.RolesNotFoundException;
import io.github.xcvqqz.cloud_file_storage.exception.UserAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;


@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final static String MISSMATCH_PASSWORDS_ERROR = "Your password and confirm password do not match";
    private final static String FORBIDDEN_ERROR_MESSAGE = "You are not authorized to access this profile";


    @ExceptionHandler(RolesNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(RolesNotFoundException ex,  HttpServletRequest request) {

        log.warn("Entity not found: URI={}, type={}, msg={}",
                request.getRequestURI(), ex.getClass().getSimpleName(), ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse
                        (HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.name(),
                        "Пользователь не найден",
                        request.getRequestURI(),
                        LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {

        log.warn("Access denied: URI={}, type={}, msg={}",
                request.getRequestURI(), ex.getClass().getSimpleName(), ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse
                        (HttpStatus.FORBIDDEN.value(),
                        HttpStatus.FORBIDDEN.name(),
                        FORBIDDEN_ERROR_MESSAGE,
                        request.getRequestURI(),
                        LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {

        log.warn("Bad Request: URI={}, type={}, msg={}",
                request.getRequestURI(), ex.getClass().getSimpleName(), ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse
                (HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.name(),
                        MISSMATCH_PASSWORDS_ERROR,
                        request.getRequestURI(),
                        LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsExceptionException(UserAlreadyExistsException ex, HttpServletRequest request) {

        log.warn("Conflict: URI={}, type={}, msg={}",
                request.getRequestURI(), ex.getClass().getSimpleName(), ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse
                (HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT.name(),
                        ex.getMessage(),
                        request.getRequestURI(),
                        LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<ErrorResponse> handleDataBaseException(DataBaseException ex, HttpServletRequest request) {

        log.error("DataBase error: URI={}, type={}, msg={}",
                request.getRequestURI(), ex.getClass().getSimpleName(), ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse
                (HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        HttpStatus.INTERNAL_SERVER_ERROR.name(),
                        ex.getMessage(),
                        request.getRequestURI(),
                        LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }



}
