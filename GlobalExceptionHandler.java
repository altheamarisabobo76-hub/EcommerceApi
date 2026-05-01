package com.ws101.abobo.eccommerceapi.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", 404,
                        "message", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAll(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", 500,
                        "message", ex.getMessage()
                )
        );
    }
}