package com.mgmtp.cfu.mapper.impl;

import com.mgmtp.cfu.dto.CourseOverviewDTO;
import com.mgmtp.cfu.entity.Course;
import com.mgmtp.cfu.mapper.DTOMapper;
import com.mgmtp.cfu.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourseOverviewMapper implements DTOMapper<CourseOverviewDTO, Course> {

    private final RegistrationService registrationService;

    @Autowired
    public CourseOverviewMapper(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Override
    public CourseOverviewDTO toDTO(Course course) {
        if (course == null) {
            return null;
        }

        CourseOverviewDTO courseOverviewDTO = CourseOverviewDTO.builder()
                .id(course.getId())
                .name(course.getName())
                .platform(course.getPlatform())
                .level(course.getLevel())
                .createdDate(course.getCreatedDate())
                .thumbnailUrl(course.getThumbnailUrl())
                .status(course.getStatus())
                .build();

        int enrollmentCount = registrationService.countLegitRegistrationInCourse(course.getId());
        courseOverviewDTO.setEnrollmentCount(enrollmentCount);

        return courseOverviewDTO;
    }
}
