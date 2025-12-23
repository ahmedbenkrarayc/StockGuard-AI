package com.stockguard.stockguard.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "status", 400,
                        "error", "Bad Request",
                        "message", errors,
                        "path", request.getRequestURI()
                ));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "status", 404,
                        "error", "Not Found",
                        "message", ex.getMessage(),
                        "path", request.getRequestURI()
                ));
    }

    @ExceptionHandler(UniqueResourceException.class)
    public ResponseEntity<Map<String, Object>> handleUniqueResource(
            UniqueResourceException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "status", 422,
                        "error", "Unprocessable Entity",
                        "message", ex.getMessage(),
                        "path", request.getRequestURI()
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "status", 400,
                        "error", "Bad Request",
                        "message", ex.getMessage(),
                        "path", request.getRequestURI()
                ));
    }

//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity<Map<String, Object>> handleAuthentication(
//            AuthenticationException ex,
//            HttpServletRequest request
//    ) {
//        return ResponseEntity
//                .status(HttpStatus.UNAUTHORIZED)
//                .body(Map.of(
//                        "timestamp", LocalDateTime.now().toString(),
//                        "status", 401,
//                        "error", "Unauthorized",
//                        "message", ex.getMessage(),
//                        "path", request.getRequestURI()
//                ));
//    }
//
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<Map<String, Object>> handleAccessDenied(
//            AccessDeniedException ex,
//            HttpServletRequest request
//    ) {
//        return ResponseEntity
//                .status(HttpStatus.FORBIDDEN)
//                .body(Map.of(
//                        "timestamp", LocalDateTime.now().toString(),
//                        "status", 403,
//                        "error", "Forbidden",
//                        "message", ex.getMessage(),
//                        "path", request.getRequestURI()
//                ));
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleInternalError(
            Exception ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "status", 500,
                        "error", "Internal Server Error",
                        "message", ex.getMessage(),
                        "path", request.getRequestURI()
                ));
    }
}
