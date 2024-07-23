package com.mgmtp.cfu.exception;


/**
 * This exception is thrown when the user is not authorized to access the resource.
 */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
