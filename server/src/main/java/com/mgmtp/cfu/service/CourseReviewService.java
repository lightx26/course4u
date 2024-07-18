package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.coursereviewdto.CourseReviewOverviewDTO;
import com.mgmtp.cfu.dto.coursereviewdto.RatingsPage;
import org.springframework.data.domain.Page;

public interface CourseReviewService {
    RatingsPage getRatingsOfCourse(Long courseId);
    Page<CourseReviewOverviewDTO> getReviewsPageOfCourse(Long courseId, Integer starFilter, int page, int size);
}
