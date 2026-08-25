package io.github.xcvqqz.cloud_file_storage.handler;

import io.github.xcvqqz.cloud_file_storage.dto.response.ErrorResponse;
import io.github.xcvqqz.cloud_file_storage.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private static final String MISSMATCH_PASSWORDS_ERROR = "Your password and confirm password do not match";
    private static final String FORBIDDEN_ERROR_MESSAGE = "You are not authorized to access this profile";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "An unexpected error occurred. Please try again later";
    private static final String CONFLICT_ERROR_MESSAGE = "A user with that name already exists";
    private static final String ROLES_NOT_FOUND_MESSAGE = "Roles not found";

    @ExceptionHandler({RolesNotFoundException.class,
            DirectoryNotFoundException.class,
            FileNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(RolesNotFoundException ex,  HttpServletRequest request) {

        log.warn("Entity not found: URI={}, type={}, msg={}",
                request.getRequestURI(), ex.getClass().getSimpleName(), ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse(HttpStatus.NOT_FOUND, ROLES_NOT_FOUND_MESSAGE, request));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {

        log.warn("Access denied: URI={}, type={}, msg={}",
                request.getRequestURI(), ex.getClass().getSimpleName(), ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(
                        buildErrorResponse(HttpStatus.FORBIDDEN, FORBIDDEN_ERROR_MESSAGE, request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {

        log.warn("Bad Request: URI={}, type={}, msg={}",
                request.getRequestURI(), ex.getClass().getSimpleName(), ex.getMessage());

        List<String> errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());


        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        buildErrorResponse(HttpStatus.BAD_REQUEST, String.join(", ", errorMessages), request));
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorResponse> handlePasswordMismatchException(PasswordMismatchException ex, HttpServletRequest request) {

        log.warn("Password and confirm password missmatch error: URI={}, type={}, msg={}",
                request.getRequestURI(), ex.getClass().getSimpleName(), ex.getMessage());


        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        buildErrorResponse(HttpStatus.BAD_REQUEST, MISSMATCH_PASSWORDS_ERROR, request));
    }


    @ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(Exception ex, HttpServletRequest request) {

        log.warn("Conflict: URI={}, type={}, msg={}",
                request.getRequestURI(), ex.getClass().getSimpleName(), ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        buildErrorResponse(HttpStatus.CONFLICT, CONFLICT_ERROR_MESSAGE, request));
    }

    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<ErrorResponse> handleDataBaseException(DataBaseException ex, HttpServletRequest request) {

        log.error("DataBase error: URI={}, type={}, msg={}",
                request.getRequestURI(), ex.getClass().getSimpleName(), ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {

        if (ex instanceof AuthenticationException ||
                ex instanceof AccessDeniedException) {
            throw (RuntimeException) ex;
        }

        log.error("INTERNAL SERVER ERROR MESSAGE: URI={}, type={}, msg={}",
                request.getRequestURI(), ex.getClass().getSimpleName(), ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MESSAGE, request));
    }

    private ErrorResponse buildErrorResponse(HttpStatus status, String message, HttpServletRequest request){
        return new ErrorResponse(
                status.value(),
                status.name(),
                message,
                request.getRequestURI(),
                LocalDateTime.now()
        );
    }

}