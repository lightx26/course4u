package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.CourseDTO;
import java.util.List;

public interface CourseService {
    public List<CourseDTO> getAvailableCourses(int pageNo, int pageSize);
}
