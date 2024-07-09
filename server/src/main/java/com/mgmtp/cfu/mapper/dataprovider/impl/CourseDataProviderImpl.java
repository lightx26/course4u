package com.mgmtp.cfu.mapper.dataprovider.impl;

import com.mgmtp.cfu.mapper.dataprovider.CourseDataProvider;
import com.mgmtp.cfu.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourseDataProviderImpl implements CourseDataProvider {
    private final RegistrationRepository registrationRepository;

    @Autowired
    public CourseDataProviderImpl(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    @Override
    public int countLegitRegistrationInCourse(Long courseId) {
        return registrationRepository.countLegitRegistrationInCourse(courseId);
    }
}
