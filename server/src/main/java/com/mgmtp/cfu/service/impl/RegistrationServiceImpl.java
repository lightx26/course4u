package com.mgmtp.cfu.service.impl;


import com.mgmtp.cfu.dto.PageResponse;
import com.mgmtp.cfu.dto.RegistrationRequest;
import com.mgmtp.cfu.dto.coursedto.CourseRequest;
import com.mgmtp.cfu.dto.coursedto.CourseResponse;
import com.mgmtp.cfu.dto.registrationdto.RegistrationDetailDTO;
import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewDTO;
import com.mgmtp.cfu.enums.CourseLevel;
import com.mgmtp.cfu.enums.RegistrationStatus;
import com.mgmtp.cfu.exception.MapperNotFoundException;
import com.mgmtp.cfu.exception.RegistrationStatusNotFoundException;
import com.mgmtp.cfu.mapper.RegistrationOverviewMapper;


import com.mgmtp.cfu.entity.Course;
import com.mgmtp.cfu.entity.Registration;
import com.mgmtp.cfu.enums.CourseStatus;
import com.mgmtp.cfu.exception.RegistrationNotFoundException;
import com.mgmtp.cfu.mapper.DTOMapper;
import com.mgmtp.cfu.mapper.factory.MapperFactory;

import com.mgmtp.cfu.repository.RegistrationRepository;
import com.mgmtp.cfu.service.CourseService;
import com.mgmtp.cfu.service.RegistrationService;
import com.mgmtp.cfu.util.AuthUtils;
import com.mgmtp.cfu.util.RegistrationValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.mgmtp.cfu.util.RegistrationOverviewUtils.getRegistrationOverviewDTOS;
import static com.mgmtp.cfu.util.RegistrationOverviewUtils.getSortedRegistrations;
import java.util.ArrayList;
@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final MapperFactory<Registration> registrationMapperFactory;
    private final RegistrationOverviewMapper registrationOverviewMapper;
    private final CourseService courseService;

    @Autowired
    public RegistrationServiceImpl(RegistrationRepository registrationRepository, RegistrationOverviewMapper registrationOverviewMapper, MapperFactory<Registration> registrationMapperFactory, CourseService courseService) {
        this.registrationRepository = registrationRepository;
        this.registrationOverviewMapper = registrationOverviewMapper;
        this.registrationMapperFactory = registrationMapperFactory;
        this.courseService = courseService;
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
        List<RegistrationStatus> excludedStatuses = List.of(RegistrationStatus.DRAFT);
        PageRequest pageRequest = PageRequest.of(page - 1, 8);
        Page<Registration> registrations = registrationRepository.findAllExceptStatus(excludedStatuses, pageRequest);

        List<RegistrationOverviewDTO> modifiedResponseContent = registrations
                .getContent()
                .stream()
                .map(registrationOverviewMapper::toDTO)
                .toList();

        return new PageImpl<>(modifiedResponseContent, pageRequest, registrations.getTotalElements());
    }
    @Transactional
    public Boolean createRegistration(RegistrationRequest registrationRequest) {
        // Create course if needed
        var modelMapper = new ModelMapper();
        CourseRequest courseRequest = CourseRequest.builder().name(registrationRequest.getName())
                .link(registrationRequest.getLink())
                .platform(registrationRequest.getPlatform())
                .thumbnailFile(registrationRequest.getThumbnailFile())
                .thumbnailUrl(registrationRequest.getThumbnailUrl())
                .teacherName(registrationRequest.getTeacherName())
                .categories(registrationRequest.getCategories())
                .level(registrationRequest.getLevel())
                .build();
        CourseResponse course = courseService.createCourse(courseRequest);
        Registration registration = Registration.builder()
                .course(modelMapper.map(course, Course.class))
                .status(RegistrationStatus.SUBMITTED)
                .registerDate(LocalDate.now())
                .duration(registrationRequest.getDuration())
                .durationUnit(registrationRequest.getDurationUnit())
                .lastUpdated(LocalDateTime.now())
                .user(AuthUtils.getCurrentUser())
                .build();
        Registration savedRegistration = registrationRepository.save(registration);
        if (savedRegistration == null) {
            throw new RuntimeException("Cannot create registration");
        }
        else {
            return true;
        }
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