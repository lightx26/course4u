package com.mgmtp.cfu.mapper;

import com.mgmtp.cfu.dto.CourseOverviewDTO;
import com.mgmtp.cfu.entity.Course;
import com.mgmtp.cfu.mapper.dataprovider.CourseDataProvider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class CourseOverviewMapper implements DTOMapper<CourseOverviewDTO, Course> {
    protected CourseDataProvider courseDataProvider;

    @Autowired
    protected void setCourseDataProvider(CourseDataProvider courseDataProvider) {
        this.courseDataProvider = courseDataProvider;
    }

    @Mapping(target = "enrollmentCount", expression = "java(courseDataProvider.countLegitRegistrationInCourse(course.getId()))")
    @Mapping(target = "rating", expression = "java(courseDataProvider.calculateAvgRating(course.getId()))")
    public abstract CourseOverviewDTO toDTO(Course course);
}
