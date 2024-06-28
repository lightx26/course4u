package com.mgmtp.cfu.util;

import com.mgmtp.cfu.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {
    public static User getCurrentUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getCredentials();
    }
}

