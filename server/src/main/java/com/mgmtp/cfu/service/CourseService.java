package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.CourseDto;
import com.mgmtp.cfu.dto.CoursePageDTO;
import com.mgmtp.cfu.enums.CoursePageSortOption;

public interface CourseService {
    CoursePageDTO getAvailableCoursesPage(int pageNo, int pageSize, CoursePageSortOption sortBy);

    CourseDto getCourseDtoById(Long id);
}
