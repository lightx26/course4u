package com.mgmtp.cfu.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MapperNotFoundException extends RuntimeException {

    public MapperNotFoundException(String message) {
        super(message);
    }
}
