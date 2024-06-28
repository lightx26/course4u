package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.PageResponse;
import com.mgmtp.cfu.dto.RegistrationOverviewPage;
import com.mgmtp.cfu.enums.RegistrationStatus;


import com.mgmtp.cfu.dto.RegistrationDetailDTO;
import com.mgmtp.cfu.entity.Registration;
import com.mgmtp.cfu.exception.RegistrationNotFoundException;

import com.mgmtp.cfu.mapper.DTOMapper;
import com.mgmtp.cfu.mapper.factory.MapperFactory;
import com.mgmtp.cfu.mapper.factory.impl.RegistrationMapperFactory;
import com.mgmtp.cfu.repository.RegistrationRepository;
import com.mgmtp.cfu.service.RegistrationService;
import com.mgmtp.cfu.util.AuthUtils;
import com.mgmtp.cfu.util.RegistrationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


import java.util.Objects;

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

    @Override
    public int countLegitRegistrationInCourse(Long courseId) {
        return registrationRepository.countLegitRegistrationInCourse(courseId);
    }

    @Override
    public PageResponse getMyRegistrationPage(int page, String status) {
        var userId = AuthUtils.getCurrentUser().getId();
        var myRegistrations = registrationRepository.getByUserId(userId);

        if (!RegistrationValidator.isDefaultStatus(status)) {
            try {
                var statusEnum = RegistrationStatus.valueOf(status.toUpperCase());
                myRegistrations=myRegistrations.stream().filter(registration -> registration.getStatus().equals(statusEnum)).toList();
            } catch (Exception e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
        var registrationPage = new RegistrationOverviewPage(page, myRegistrations);
        var listOfMyRegistration = registrationPage.getDtoPage();
        listOfMyRegistration.sort((o1, o2) -> {
            if (Objects.isNull(o1.getLastUpdate())) o1.setLastUpdate(o1.getRegisterDate().atStartOfDay());
            if (Objects.isNull(o2.getLastUpdate())) o2.setLastUpdate(o2.getRegisterDate().atStartOfDay());
            return o2.getLastUpdate().compareTo(o1.getLastUpdate());
        });
        return PageResponse.builder().list(listOfMyRegistration).totalElements(myRegistrations.size()).build();
    }
}
