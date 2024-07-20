package com.mgmtp.cfu.exception;

import com.mgmtp.cfu.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ BadCredentialsException.class,NotExistEmailException.class})
    public ResponseEntity<?> handleIllegalArgumentException(final Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder().message(e.getMessage()).status("400").build());}

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleCourseNotFoundException(CourseNotFoundException cnfe) {
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.value()+"", cnfe.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({IllegalStateException.class,IllegalArgumentException.class})
    public ResponseEntity<?> handleIllegalStateException(final Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.builder().message(e.getMessage()).status("500").build());
    }
    @ExceptionHandler({ServerErrorRuntimeException.class})
    public ResponseEntity<?> handleException(final Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage(),"500"));
    }
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> handleAccessDeniedException(final Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse.builder().message(e.getMessage()).status("403").build());
    }
    @ExceptionHandler(AccountExistByEmailException.class)
    public ResponseEntity<?> handleAccountExistByEmailException (AccountExistByEmailException e)
    {
        return ResponseEntity.status(409).body(ErrorResponse.builder().message(e.getMessage()).status("409").build());
    }
    @ExceptionHandler(BadRequestRuntimeException.class)
    public ResponseEntity<?> handleBadRequestRunTimeException(final Exception e) {
        return ResponseEntity.status(400).body(ErrorResponse.builder().message(e.getMessage()).status("400").build());
    }
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbiddenException(final Exception e) {
        return ResponseEntity.status(403).body(ErrorResponse.builder().message(e.getMessage()).status("403").build());
    }
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(413).body(ErrorResponse.builder().message(exc.getMessage()).status("413").build());

    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(Exception e){
        return ResponseEntity.status(400).body(ErrorResponse.builder().message(e.getMessage()).status("400").build());
    }

}
