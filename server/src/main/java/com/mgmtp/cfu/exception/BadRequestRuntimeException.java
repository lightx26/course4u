package com.mgmtp.cfu.exception;

public class BadRequestRuntimeException extends RuntimeException{
    public BadRequestRuntimeException(String message) {
        super(message);
    }
}
