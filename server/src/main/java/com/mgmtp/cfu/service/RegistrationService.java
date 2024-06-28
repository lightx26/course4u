package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.PageResponse;

import com.mgmtp.cfu.dto.RegistrationDetailDTO;

public interface RegistrationService {
    RegistrationDetailDTO getDetailRegistration(Long id);
    int countLegitRegistrationInCourse(Long courseId);

    PageResponse getMyRegistrationPage(int page, String status);
}
