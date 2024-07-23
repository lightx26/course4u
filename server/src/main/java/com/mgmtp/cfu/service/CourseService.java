package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.coursedto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseService {

    Page<CourseOverviewDTO> getAvailableCoursesPage(CourseSearchRequest request);

    CourseDto getCourseDtoById(Long id);

    CourseResponse createCourse(CourseRequest courseDTO);

    void deleteCourseById(Long id);

    List<CourseOverviewDTO> getRelatedCourses(Long courseId);

}
