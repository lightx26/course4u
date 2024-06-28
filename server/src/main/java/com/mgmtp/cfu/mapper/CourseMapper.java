package com.mgmtp.cfu.mapper;

import com.mgmtp.cfu.dto.CourseOverviewDTO;
import com.mgmtp.cfu.entity.Course;
import com.mgmtp.cfu.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    private final RegistrationService registrationService;

    @Autowired
    public CourseMapper(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    public CourseOverviewDTO toOverviewDTO(Course course) {
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
