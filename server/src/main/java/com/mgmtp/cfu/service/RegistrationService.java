package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.PageResponse;

import com.mgmtp.cfu.dto.registrationdto.RegistrationDetailDTO;
import com.mgmtp.cfu.entity.Registration;
import org.springframework.data.domain.Page;

import com.mgmtp.cfu.dto.RegistrationDetailDTO;

public interface RegistrationService {
    RegistrationDetailDTO getDetailRegistration(Long id);
    PageResponse getMyRegistrationPage(int page, String status);
    Page<Registration> getRegistrationByStatus(int page, String status);
    Page<Registration> getAllRegistrations(int page);
}
