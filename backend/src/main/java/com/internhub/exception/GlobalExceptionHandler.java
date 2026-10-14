package com.internhub.exception;

import com.internhub.dto.ApiError;
import com.internhub.exception.DuplicateApplicationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> fieldErrors.putIfAbsent(error.getField(), error.getDefaultMessage()));
        return error(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Please correct the highlighted fields.", fieldErrors, request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException exception, HttpServletRequest request) {
        return error(HttpStatus.NOT_FOUND, "NOT_FOUND", exception.getMessage(), Map.of(), request);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleDuplicateEmail(EmailAlreadyExistsException exception, HttpServletRequest request) {
        return error(HttpStatus.CONFLICT, "EMAIL_ALREADY_EXISTS", exception.getMessage(), Map.of(), request);
    }

    @ExceptionHandler(DuplicateApplicationException.class)
    public ResponseEntity<ApiError> handleDuplicateApplication(DuplicateApplicationException exception, HttpServletRequest request) {
        return error(HttpStatus.CONFLICT, "DUPLICATE_APPLICATION", exception.getMessage(), Map.of(), request);
    }

    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<ApiError> handleInvalidTransition(InvalidStatusTransitionException exception, HttpServletRequest request) {
        return error(HttpStatus.CONFLICT, "INVALID_STATUS_TRANSITION", exception.getMessage(), Map.of(), request);
    }

    @ExceptionHandler(InternshipClosedException.class)
    public ResponseEntity<ApiError> handleClosedInternship(InternshipClosedException exception, HttpServletRequest request) {
        return error(HttpStatus.CONFLICT, "INTERNSHIP_CLOSED", exception.getMessage(), Map.of(), request);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleInvalidState(IllegalStateException exception, HttpServletRequest request) {
        return error(HttpStatus.CONFLICT, "INVALID_INTERNSHIP_STATE", exception.getMessage(), Map.of(), request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthentication(AuthenticationException exception, HttpServletRequest request) {
        return error(HttpStatus.UNAUTHORIZED, "UNAUTHENTICATED", "Invalid email or password.", Map.of(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception exception, HttpServletRequest request) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Something went wrong. Please try again.", Map.of(), request);
    }

    private ResponseEntity<ApiError> error(HttpStatus status, String code, String message, Map<String, String> fieldErrors, HttpServletRequest request) {
        return ResponseEntity.status(status).body(new ApiError(Instant.now(), status.value(), code, message, fieldErrors, request.getRequestURI()));
    }
}
