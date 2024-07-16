package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.PageResponse;
import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewDTO;
import com.mgmtp.cfu.enums.RegistrationStatus;
import com.mgmtp.cfu.exception.MapperNotFoundException;
import com.mgmtp.cfu.mapper.RegistrationDetailMapper;
import com.mgmtp.cfu.mapper.RegistrationOverviewMapper;

import com.mgmtp.cfu.dto.registrationdto.RegistrationDetailDTO;
import com.mgmtp.cfu.entity.Registration;
import com.mgmtp.cfu.exception.RegistrationNotFoundException;
import com.mgmtp.cfu.exception.RegistrationStatusNotFoundException;
import com.mgmtp.cfu.mapper.DTOMapper;
import com.mgmtp.cfu.mapper.factory.MapperFactory;
import com.mgmtp.cfu.repository.RegistrationRepository;
import com.mgmtp.cfu.service.RegistrationService;
import com.mgmtp.cfu.util.AuthUtils;
import com.mgmtp.cfu.util.RegistrationValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.mgmtp.cfu.util.RegistrationOverviewUtils.getRegistrationOverviewDTOS;
import static com.mgmtp.cfu.util.RegistrationOverviewUtils.getSortedRegistrations;


import java.util.ArrayList;


@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final MapperFactory<Registration> registrationMapperFactory;
    private final RegistrationDetailMapper registrationDetailMapper;
    private final RegistrationOverviewMapper registrationOverviewMapper;

    @Autowired
    public RegistrationServiceImpl(RegistrationRepository registrationRepository, RegistrationOverviewMapper registrationOverviewMapper, MapperFactory<Registration> registrationMapperFactory, RegistrationDetailMapper registrationDetailMapper) {
        this.registrationRepository = registrationRepository;
        this.registrationOverviewMapper = registrationOverviewMapper;
        this.registrationMapperFactory = registrationMapperFactory;
        this.registrationDetailMapper = registrationDetailMapper;
    }

    @Override
    public RegistrationDetailDTO getDetailRegistration(Long id) {
        Optional<DTOMapper<RegistrationDetailDTO, Registration>> registrationDtoMapperOpt = registrationMapperFactory.getDTOMapper(RegistrationDetailDTO.class);

        if (registrationDtoMapperOpt.isEmpty()) {
            throw new MapperNotFoundException("No mapper found for registrationDtoMapperOpt");
        }
        Registration registration = registrationRepository.findById(id).orElseThrow(() -> new RegistrationNotFoundException("Registration not found"));
        return registrationDtoMapperOpt.get().toDTO(registration);
    }

    @Override
    public PageResponse getMyRegistrationPage(int page, String status) {
        status = status.trim();
        var userId = AuthUtils.getCurrentUser().getId();

        var myRegistrations = getSortedRegistrations(userId, registrationRepository);

        if (!RegistrationValidator.isDefaultStatus(status)) {
            try {
                var statusEnum = RegistrationStatus.valueOf(status.toUpperCase());
                myRegistrations = myRegistrations.stream().filter(registration -> registration.getStatus().equals(statusEnum)).toList();
            } catch (Exception e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
        if (myRegistrations == null || myRegistrations.isEmpty()) {
            return PageResponse.builder().totalElements(0).list(new ArrayList<>()).build();
        }
        var listOfMyRegistration = getRegistrationOverviewDTOS(page, myRegistrations, registrationMapperFactory.getDTOMapper(RegistrationOverviewDTO.class));
        return PageResponse.builder().list(listOfMyRegistration).totalElements(myRegistrations.size()).build();
    }

    @Override
    public Page<RegistrationOverviewDTO> getAllRegistrations(int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 8);
        Page<Registration> registrations = registrationRepository.findAll(pageRequest);

        List<RegistrationOverviewDTO> modifiedResponseContent = registrations
                .getContent()
                .stream()
                .map(registrationOverviewMapper::toDTO)
                .toList();

        return new PageImpl<>(modifiedResponseContent, pageRequest, registrations.getTotalElements());
    }

    @Override
    public Page<RegistrationOverviewDTO> getRegistrationByStatus(int page, String status) {
        try{
            PageRequest pageRequest = PageRequest.of(page - 1, 8);
            RegistrationStatus registrationStatus = RegistrationStatus.valueOf(status.toUpperCase());

            Page<Registration> registrations = registrationRepository.findAllByStatus(registrationStatus, pageRequest);

            List<RegistrationOverviewDTO> modifiedResponseContent = registrations
                    .getContent()
                    .stream()
                    .map(registrationOverviewMapper::toDTO)
                    .toList();

            return new PageImpl<>(modifiedResponseContent, pageRequest, registrations.getTotalElements());
        }
        catch (IllegalArgumentException e){
            throw new RegistrationStatusNotFoundException("Status not found");
        }
    }

}