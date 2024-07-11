package com.mgmtp.cfu.mapper.dataprovider.impl;

import com.mgmtp.cfu.mapper.dataprovider.CourseDataProvider;
import com.mgmtp.cfu.repository.CourseRepository;
import com.mgmtp.cfu.util.RegistrationStatusUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourseDataProviderImpl implements CourseDataProvider {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseDataProviderImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public int countLegitRegistrationInCourse(Long courseId) {
        return courseRepository.countRegistrationInCourse(courseId, RegistrationStatusUtil.ACCEPTED_STATUSES);
    }

    @Override
    public Double calculateAvgRating(Long courseId) {
        return courseRepository.calculateAvgRating(courseId);
    }
}
