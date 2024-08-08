package com.mgmtp.cfu.util;

import com.mgmtp.cfu.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class AuthUtils {
    public static User getCurrentUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getCredentials();
    }
    public static String getUserName(User user){
        var fullName=user.getFullName();
        var username=user.getUsername();
        return Objects.nonNull(fullName)?!fullName.isEmpty()?fullName:username:username;
    }
}

