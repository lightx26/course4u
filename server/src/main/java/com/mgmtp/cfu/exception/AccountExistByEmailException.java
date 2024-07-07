package com.mgmtp.cfu.exception;

public class AccountExistByEmailException extends RuntimeException {
    public AccountExistByEmailException(String message) {
        super(message);
    }
}
