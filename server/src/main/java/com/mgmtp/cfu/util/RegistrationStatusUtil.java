package com.mgmtp.cfu.util;

import com.mgmtp.cfu.enums.RegistrationStatus;

import java.util.List;

public class RegistrationStatusUtil {
    public static final List<RegistrationStatus> ACCEPTED_STATUSES = List.of(
            RegistrationStatus.DONE,
            RegistrationStatus.VERIFYING,
            RegistrationStatus.VERIFIED,
            RegistrationStatus.DOCUMENT_DECLINED,
            RegistrationStatus.CLOSED
    );

    private RegistrationStatusUtil() {
        // Private constructor to prevent instantiation
    }
}
