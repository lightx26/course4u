package com.mgmtp.cfu.mapper;

import com.mgmtp.cfu.dto.CourseOverviewDTO;
import com.mgmtp.cfu.entity.Course;
import com.mgmtp.cfu.service.RegistrationService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class CourseOverviewMapper implements DTOMapper<CourseOverviewDTO, Course> {
    @Autowired
    protected RegistrationService registrationService;

    @Mapping(target = "enrollmentCount", expression = "java(registrationService.countLegitRegistrationInCourse(course.getId()))")
    public abstract CourseOverviewDTO toDTO(Course course);
}
