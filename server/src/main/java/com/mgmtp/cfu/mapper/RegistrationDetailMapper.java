package com.mgmtp.cfu.mapper;

import com.mgmtp.cfu.dto.RegistrationDetailDTO;
import com.mgmtp.cfu.entity.Registration;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public abstract class RegistrationDetailMapper implements DTOMapper<RegistrationDetailDTO, Registration> {
    public abstract RegistrationDetailDTO toDTO(Registration registration);
}
