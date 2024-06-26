package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.PageResponse;

import com.mgmtp.cfu.dto.registrationdto.RegistrationDetailDTO;
import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewDTO;
import org.springframework.data.domain.Page;
import com.mgmtp.cfu.dto.PageResponse;
import com.mgmtp.cfu.dto.RegistrationRequest;
import com.mgmtp.cfu.dto.registrationdto.RegistrationDetailDTO;
import com.mgmtp.cfu.entity.Registration;


public interface RegistrationService {
    RegistrationDetailDTO getDetailRegistration(Long id);
    PageResponse getMyRegistrationPage(int page, String status);
    Boolean createRegistration(RegistrationRequest registrationRequest);

    Page<RegistrationOverviewDTO> getAllRegistrations(int page);
    Page<RegistrationOverviewDTO> getRegistrationByStatus(int page, String status);
}
