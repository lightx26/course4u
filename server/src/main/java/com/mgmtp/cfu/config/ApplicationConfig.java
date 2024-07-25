package com.mgmtp.cfu.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class ApplicationConfig {
    @PostConstruct
    void configTimeZone(){
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Lisbon"));
    }
}
