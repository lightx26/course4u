package com.mgmtp.cfu.exception;

import com.mgmtp.cfu.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responsePre + e.getMessage() + "\"}"); }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleCourseNotFoundException(CourseNotFoundException cnfe) {
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), cnfe.getMessage(), ErrorResponse.now("yyyy-MM-dd.'T'HH:mm:ss"));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<?> handleIllegalStateException(final Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responsePre + e.getMessage() + "\"}");
    }

    @ExceptionHandler(AccountExistByEmailException.class)
    public ResponseEntity<?> handleAccountExistByEmailException (AccountExistByEmailException e)
    {
        return ResponseEntity.status(409).body(ErrorResponse.builder().message(e.getMessage()).status("409").build());
    }

    @ExceptionHandler(AccountExistByEmailException.class)
    public ResponseEntity<?> handleAccountExistByEmailException (AccountExistByEmailException e)
    {
        return ResponseEntity.status(409).body(responsePre + e.getMessage() + "\"}");
    }

}
