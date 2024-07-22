package com.mgmtp.cfu.service;

import com.mgmtp.cfu.entity.Registration;

public interface RegistrationFeedbackService {
    void sendFeedback(Registration registration, String comment);
}
