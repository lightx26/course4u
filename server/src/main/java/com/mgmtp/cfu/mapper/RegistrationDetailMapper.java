package com.mgmtp.cfu.mapper;

import com.mgmtp.cfu.dto.registrationdto.RegistrationDetailDTO;
import com.mgmtp.cfu.entity.Registration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public abstract class RegistrationDetailMapper implements DTOMapper<RegistrationDetailDTO, Registration>, EntityMapper<RegistrationDetailDTO, Registration> {
    @Mapping(target = "course.platform", source = "registration.course.platform.label")
    public abstract RegistrationDetailDTO toDTO(Registration registration);
    public abstract Registration toEntity(RegistrationDetailDTO registrationDetailDTO);
}
