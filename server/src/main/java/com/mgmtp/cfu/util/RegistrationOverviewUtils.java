package com.mgmtp.cfu.util;

import com.mgmtp.cfu.dto.RegistrationOverviewDTO;
import com.mgmtp.cfu.dto.RegistrationOverviewPage;
import com.mgmtp.cfu.entity.Registration;
import com.mgmtp.cfu.mapper.RegistrationOverviewMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegistrationOverviewUtils {
    public static List<RegistrationOverviewDTO> getRegistrationOverviewDTOS(int page, List<Registration> myRegistrations, RegistrationOverviewMapper registrationOverviewMapper) {
        var registrationPage = new RegistrationOverviewPage(page, myRegistrations, Constant.MY_REGISTRATION_PAGE_LIMIT,registrationOverviewMapper);
        var listOfMyRegistration = registrationPage.getDtoPage();
        try {
            listOfMyRegistration.sort((o1, o2) -> {
                if (Objects.isNull(o1.getLastUpdate())) o1.setLastUpdate(o1.getRegisterDate().atStartOfDay());
                if (Objects.isNull(o2.getLastUpdate())) o2.setLastUpdate(o2.getRegisterDate().atStartOfDay());
                return o2.getLastUpdate().compareTo(o1.getLastUpdate());
            });
            return listOfMyRegistration;
        }catch (Exception e){
            return new ArrayList<>();
        }
    }
}
