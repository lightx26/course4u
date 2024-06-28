package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.CourseOverviewDTO;
import java.util.List;

public interface CourseService {
    List<CourseOverviewDTO> getAvailableCoursesPage(int pageNo, int pageSize);
    int getAvailableCoursesPageCount(int pageSize);
}
