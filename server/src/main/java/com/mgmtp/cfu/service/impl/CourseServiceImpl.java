package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.CourseDTO;
import com.mgmtp.cfu.entity.Course;
import com.mgmtp.cfu.enums.CourseStatus;
import com.mgmtp.cfu.mapper.CourseMapper;
import com.mgmtp.cfu.repository.CourseRepository;
import com.mgmtp.cfu.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    private Page<Course> getAvailableCourses(int pageNo, int pageSize) {
        // Make sure pageNo is at least 1
        pageNo = Math.max(pageNo, 1);

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return courseRepository.findByStatus(CourseStatus.AVAILABLE, pageable);
    }

    @Override
    public List<CourseDTO> getAvailableCoursesPage(int pageNo, int pageSize) {
        // Make sure pageNo is at most the number of pages
        pageNo = Math.min(pageNo, getAvailableCoursesPageCount(pageSize));

        Page<Course> courseList = getAvailableCourses(pageNo, pageSize);
        return courseList.map(CourseMapper::toDTO).getContent();
    }

    @Override
    public int getAvailableCoursesPageCount(int pageSize) {
        Page<Course> courseList = getAvailableCourses(1, pageSize);
        return courseList.getTotalPages();
    }
}
