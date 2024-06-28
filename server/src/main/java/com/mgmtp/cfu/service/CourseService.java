package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.CourseDto;
import com.mgmtp.cfu.dto.CoursePageDTO;

public interface CourseService {
    CoursePageDTO getAvailableCoursesPage(int pageNo, int pageSize);

    CourseDto getCourseDtoById(Long id);

}
