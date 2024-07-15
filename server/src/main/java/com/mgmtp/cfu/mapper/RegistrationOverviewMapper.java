package com.mgmtp.cfu.mapper;

import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewDTO;
import com.mgmtp.cfu.entity.Registration;
import org.springframework.stereotype.Component;

@Component
public class RegistrationOverviewMapper implements DTOMapper<RegistrationOverviewDTO, Registration>{
    @Override
    public RegistrationOverviewDTO toDTO(Registration entity) {
        var registrationDTO= RegistrationOverviewDTO.builder()
                .id(entity.getId())
                .endDate(entity.getEndDate())
                .startDate(entity.getStartDate())
                .status(entity.getStatus())
                .lastUpdate(entity.getLastUpdate())
                .courseName(entity.getCourse().getName())
                .courseId(entity.getCourse().getId())
                .registerDate(entity.getRegisterDate())
                .userFullName(entity.getUser().getFullName())
                .userId(entity.getUser().getId())
                .username(entity.getUser().getUsername())
                .thumbnailUrl(entity.getCourse().getThumbnailUrl())
                .build();
        return registrationDTO;
    }
}
