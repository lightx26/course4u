package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.coursedto.*;
import com.mgmtp.cfu.enums.CoursePageSortOption;
import org.springframework.data.domain.Page;

public interface CourseService {
    Page<CourseOverviewDTO> getAvailableCoursesPage(String search, CoursePageFilter filter, CoursePageSortOption sortBy, int pageNo, int pageSize);
    CourseDto getCourseDtoById(Long id);
    CourseResponse createCourse(CourseRequest courseDTO);

    void deleteCourseById(Long id);
}
