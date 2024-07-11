package com.mgmtp.cfu.exception;

public class NotExistEmailException extends RuntimeException{
    public NotExistEmailException(String message) {
        super(message);
    }
}
