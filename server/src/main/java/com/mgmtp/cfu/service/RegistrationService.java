package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.PageResponse;

import com.mgmtp.cfu.dto.registrationdto.FeedbackRequest;
import com.mgmtp.cfu.dto.registrationdto.RegistrationDetailDTO;
import com.mgmtp.cfu.dto.registrationdto.RegistrationEnrollDTO;
import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewDTO;
import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewParams;
import org.springframework.data.domain.Page;
import com.mgmtp.cfu.dto.RegistrationRequest;

import java.util.Map;

public interface RegistrationService {

    RegistrationDetailDTO getDetailRegistration(Long id);

    PageResponse getMyRegistrationPage(int page, String status);

    Boolean createRegistration(RegistrationRequest registrationRequest);

    void finishRegistration(Long id);

    void approveRegistration(Long id);

    void declineRegistration(Long id , FeedbackRequest feedbackRequest);

    void closeRegistration(Long id, FeedbackRequest feedbackRequest);

    void deleteRegistration(Long id);

    boolean startLearningCourse(Long courseId);

    void discardRegistration(Long id);

    // Admin Registration Services
    Page<RegistrationOverviewDTO> getRegistrations(RegistrationOverviewParams params, int page, int pageSize);

    void createRegistrationFromExistingCourses(Long courseId, RegistrationEnrollDTO registrationEnrollDTO,boolean asDraft);

    void verifyRegistration(Long id, Map<String, String> longDocumentStatusMap, String status);

    void editRegistration(Long id, RegistrationRequest registrationRequest,boolean asDraft);

    Boolean isExistAvailableCourse(Long id);

    void createRegistrationAsDraft(RegistrationRequest registrationRequest);
}
