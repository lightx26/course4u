package com.mgmtp.cfu.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RegistrationExceptionHandler {
    @ExceptionHandler(RegistrationNotFoundException.class)
    public ResponseEntity handleRegistrationNotFoundException(RegistrationNotFoundException e) {
        return ResponseEntity.status(404).body("{\"message\": \"" + e.getMessage() + "\"}" );
    }
}
