package com.mgmtp.cfu.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final String responsePre = "{\"message\":\"";

    @ExceptionHandler({InternalAuthenticationServiceException.class})
    public ResponseEntity<?> handleException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responsePre +"Server encountered an internal error.\"}" );
    }
    @ExceptionHandler({IllegalArgumentException.class, BadCredentialsException.class})
    public ResponseEntity<?> handleIllegalArgumentException(final Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responsePre + e.getMessage() + "\"}");
    }

}
