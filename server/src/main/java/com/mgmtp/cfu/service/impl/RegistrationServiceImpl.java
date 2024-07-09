package com.mgmtp.cfu.service.impl;



import com.mgmtp.cfu.dto.RegistrationDetailDTO;
import com.mgmtp.cfu.entity.Registration;
import com.mgmtp.cfu.exception.RegistrationNotFoundException;

import com.mgmtp.cfu.mapper.DTOMapper;
import com.mgmtp.cfu.mapper.factory.MapperFactory;
import com.mgmtp.cfu.mapper.factory.impl.RegistrationMapperFactory;
import com.mgmtp.cfu.repository.RegistrationRepository;
import com.mgmtp.cfu.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final MapperFactory<Registration> registrationMapperFactory;

    @Autowired
    public RegistrationServiceImpl(RegistrationRepository registrationRepository,MapperFactory<Registration> registrationMapperFactory) {
        this.registrationRepository = registrationRepository;
        this.registrationMapperFactory = registrationMapperFactory;
    }

    @Override
    public RegistrationDetailDTO getDetailRegistration(Long id) {
        Optional<DTOMapper<RegistrationDetailDTO, Registration>> registrationDtoMapperOpt = registrationMapperFactory.getDTOMapper(RegistrationDetailDTO.class);

        if (registrationDtoMapperOpt.isEmpty()) {
            throw new IllegalStateException("No mapper found for registrationDtoMapperOpt");
        }
        Registration registration = registrationRepository.findById(id).orElseThrow(() -> new RegistrationNotFoundException("Registration not found"));
        return registrationDtoMapperOpt.get().toDTO(registration);
    }
}
