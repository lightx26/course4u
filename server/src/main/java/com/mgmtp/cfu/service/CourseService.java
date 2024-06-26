package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.CourseDto;
import com.mgmtp.cfu.dto.CoursePageDTO;
import com.mgmtp.cfu.enums.CoursePageSortOption;
import com.mgmtp.cfu.dto.CourseRequest;
import com.mgmtp.cfu.dto.CourseResponse;

public interface CourseService {
    CoursePageDTO getAvailableCoursesPage(int pageNo, int pageSize, CoursePageSortOption sortBy);

    CourseDto getCourseDtoById(Long id);
    CourseResponse createCourse(CourseRequest courseDTO);
}
