package com.mgmtp.cfu.mapper.dataprovider;

public interface CourseDataProvider {
    int countLegitRegistrationInCourse(Long courseId);
    Double calculateAvgRating(Long courseId);
}
