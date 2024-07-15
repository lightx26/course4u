package com.mgmtp.cfu.util;

import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewDTO;
import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewPage;
import com.mgmtp.cfu.entity.Registration;
import com.mgmtp.cfu.mapper.RegistrationOverviewMapper;
import com.mgmtp.cfu.repository.RegistrationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;

import java.util.List;

@Slf4j
public class RegistrationOverviewUtils {
    public static List<RegistrationOverviewDTO> getRegistrationOverviewDTOS(int page, List<Registration> myRegistrations, RegistrationOverviewMapper registrationOverviewMapper) {
        var registrationPage = new RegistrationOverviewPage(page, myRegistrations, Constant.MY_REGISTRATION_PAGE_LIMIT, registrationOverviewMapper);
        return registrationPage.getDtoPage();
    }
    public static List<Registration> getSortedRegistrations(Long userId, RegistrationRepository registrationRepository) {
        Sort sort = Sort.by(
                Sort.Order.desc("lastUpdate").nullsLast(),
                Sort.Order.desc("endDate").nullsLast(),
                Sort.Order.desc("startDate").nullsLast(),
                Sort.Order.desc("registerDate").nullsLast()
        );

        return registrationRepository.getByUserId(userId, sort);
    }
}
