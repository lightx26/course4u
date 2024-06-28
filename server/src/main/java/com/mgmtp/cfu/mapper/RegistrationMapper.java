package com.mgmtp.cfu.mapper;

import com.mgmtp.cfu.dto.MyRegistrationOverviewDTO;
import com.mgmtp.cfu.entity.Registration;
public class RegistrationMapper {
    public static MyRegistrationOverviewDTO toMyRegistrationDto(Registration registration) {
        var registrationDTO= MyRegistrationOverviewDTO.builder()
                .id(registration.getId())
                .endDate(registration.getEndDate())
                .startDate(registration.getStartDate())
                .status(registration.getStatus())
                .lastUpdate(registration.getLastUpdate())
                .courseName(registration.getCourse().getName())
                .courseId(registration.getCourse().getId())
                .registerDate(registration.getRegisterDate())
                .build();
        return registrationDTO;
    }
}
