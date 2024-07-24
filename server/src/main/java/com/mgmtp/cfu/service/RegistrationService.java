package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.PageResponse;

import com.mgmtp.cfu.dto.registrationdto.FeedbackRequest;
import com.mgmtp.cfu.dto.registrationdto.RegistrationDetailDTO;
import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewDTO;
import org.springframework.data.domain.Page;
import com.mgmtp.cfu.dto.RegistrationRequest;

public interface RegistrationService {
    RegistrationDetailDTO getDetailRegistration(Long id);
    PageResponse getMyRegistrationPage(int page, String status);
    Boolean createRegistration(RegistrationRequest registrationRequest);

    Page<RegistrationOverviewDTO> getAllRegistrations(int page);
    Page<RegistrationOverviewDTO> getRegistrationByStatus(int page, String status);
    void approveRegistration(Long id);
    void declineRegistration(Long id , FeedbackRequest feedbackRequest);
    void closeRegistration(Long id, FeedbackRequest feedbackRequest);
    void deleteRegistration(Long id);
    boolean startLearningCourse(Long courseId);
    void discardRegistration(Long id);
}
