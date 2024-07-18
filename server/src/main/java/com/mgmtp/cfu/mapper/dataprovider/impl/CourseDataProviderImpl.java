package com.mgmtp.cfu.mapper.dataprovider.impl;

import com.mgmtp.cfu.mapper.dataprovider.CourseDataProvider;
import com.mgmtp.cfu.repository.CourseReviewRepository;
import com.mgmtp.cfu.repository.RegistrationRepository;
import com.mgmtp.cfu.util.RegistrationStatusUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourseDataProviderImpl implements CourseDataProvider {
    private final RegistrationRepository registrationRepository;
    private final CourseReviewRepository courseReviewRepository;

    @Autowired
    public CourseDataProviderImpl(RegistrationRepository registrationRepository, CourseReviewRepository courseReviewRepository) {
        this.registrationRepository = registrationRepository;
        this.courseReviewRepository = courseReviewRepository;
    }

    @Override
    public int countLegitRegistrationInCourse(Long courseId) {
        return registrationRepository.countRegistrationInCourse(courseId, RegistrationStatusUtil.ACCEPTED_STATUSES);
    }

    @Override
    public Double calculateAvgRating(Long courseId) {
        return courseReviewRepository.calculateAvgRating(courseId);
    }
}
