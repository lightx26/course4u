package com.mgmtp.cfu.util;

import java.util.Objects;

public class RegistrationValidator {
    public static boolean isDefaultStatus(String status) {
        if(Objects.isNull(status) || status.trim().isEmpty())
            return true;
        return status.toUpperCase().equals("ALL") || status.toLowerCase().equals("default");
    }
}
