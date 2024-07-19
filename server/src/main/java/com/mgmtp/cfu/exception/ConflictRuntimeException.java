package com.mgmtp.cfu.exception;

/*
This Exception was thrown when action change or create a change on server but change existed.
 */
public class ConflictRuntimeException extends RuntimeException {
    public ConflictRuntimeException(String message) {
        super(message);
    }
}
