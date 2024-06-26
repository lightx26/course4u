package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.CourseDTO;
import java.util.List;

public interface CourseService {
    List<CourseDTO> getAvailableCoursesPage(int pageNo, int pageSize);
    int getAvailableCoursesPageCount(int pageSize);
}
