package com.mgmtp.cfu.enums;

import lombok.Getter;

@Getter
public enum CoursePlatform {
    UDEMY("Udemy"),
    COURSERA("Coursera"),
    LINKEDIN("LinkedIn");

    private final String label;

    CoursePlatform(String label) {
        this.label = label;
    }
}
