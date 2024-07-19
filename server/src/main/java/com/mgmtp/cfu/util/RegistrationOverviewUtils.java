package com.mgmtp.cfu.util;

import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewDTO;
import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewPage;
import com.mgmtp.cfu.entity.Registration;
import com.mgmtp.cfu.enums.RegistrationStatus;
import com.mgmtp.cfu.mapper.DTOMapper;
import com.mgmtp.cfu.repository.RegistrationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@Slf4j
public class RegistrationOverviewUtils {
    public static List<RegistrationOverviewDTO> getRegistrationOverviewDTOS(int page, List<Registration> myRegistrations, Optional<DTOMapper<RegistrationOverviewDTO, Registration>> registrationOverviewMapper) {
        var registrationPage = new RegistrationOverviewPage(page, myRegistrations, Constant.MY_REGISTRATION_PAGE_LIMIT, registrationOverviewMapper);

        return registrationPage.getDtoPage().stream()
                .peek(registrationOverviewDTO -> {
                    var yetStartStatus=List.of(RegistrationStatus.DRAFT, RegistrationStatus.SUBMITTED,RegistrationStatus.DECLINED);
                    if(registrationOverviewDTO!=null && yetStartStatus.contains(registrationOverviewDTO.getStatus())) {
                        registrationOverviewDTO.setStartDate(null);
                        registrationOverviewDTO.setEndDate(null);
                    }
               }).toList();
    }
    public static List<Registration> getSortedRegistrations(Long userId, RegistrationRepository registrationRepository) {
        Sort sort = Sort.by(
                Sort.Order.desc("lastUpdated").nullsLast(),
                Sort.Order.desc("endDate").nullsLast(),
                Sort.Order.desc("startDate").nullsLast(),
                Sort.Order.desc("registerDate").nullsLast()
        );

        return registrationRepository.getByUserId(userId, sort);
    }
}
