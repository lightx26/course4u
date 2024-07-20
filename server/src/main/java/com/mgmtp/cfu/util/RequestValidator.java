package com.mgmtp.cfu.util;

import com.mgmtp.cfu.exception.BadRequestRuntimeException;

import java.util.Objects;

public class RequestValidator {
    public static void validateId(Long id, String idName){
        if(Objects.isNull(id))
            throw new BadRequestRuntimeException("The "+idName+" must be not null.");
    }

}
