package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.entity.Registration;
import com.mgmtp.cfu.entity.RegistrationFeedback;
import com.mgmtp.cfu.repository.RegistrationFeedbackRepository;
import com.mgmtp.cfu.service.RegistrationFeedbackService;
import com.mgmtp.cfu.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegistrationFeedbackServiceImpl implements RegistrationFeedbackService {

    private final RegistrationFeedbackRepository registrationFeedbackRepository;

    @Autowired
    public RegistrationFeedbackServiceImpl(RegistrationFeedbackRepository registrationFeedbackRepository) {
        this.registrationFeedbackRepository = registrationFeedbackRepository;
    }

    @Override
    public void sendFeedback(Registration registration, String comment) {
        RegistrationFeedback registrationFeedback = RegistrationFeedback.builder()
                .user(AuthUtils.getCurrentUser())
                .registration(registration)
                .comment(comment)
                .createdDate(LocalDateTime.now())
                .build();

        registrationFeedbackRepository.save(registrationFeedback);
    }
}
