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

    public static final List<RegistrationStatus> CLOSABLE_STATUSES = List.of(
            RegistrationStatus.DONE,
            RegistrationStatus.VERIFYING,
            RegistrationStatus.DOCUMENT_DECLINED,
            RegistrationStatus.VERIFIED
    );

    public static boolean isCloseableStatus(RegistrationStatus status) {
        return CLOSABLE_STATUSES.contains(status);
    }
}
