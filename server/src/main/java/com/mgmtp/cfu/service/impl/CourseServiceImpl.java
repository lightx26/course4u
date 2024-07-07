package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.CourseOverviewDTO;
import com.mgmtp.cfu.dto.CoursePageDTO;
import com.mgmtp.cfu.entity.Course;
import com.mgmtp.cfu.enums.CourseStatus;
import com.mgmtp.cfu.mapper.DTOMapper;
import com.mgmtp.cfu.mapper.factory.MapperFactory;
import com.mgmtp.cfu.mapper.factory.impl.CourseMapperFactory;
import com.mgmtp.cfu.repository.CourseRepository;
import com.mgmtp.cfu.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    @Qualifier("CourseMapper")
    private final MapperFactory courseMapperFactory;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, MapperFactory courseMapperFactory) {
        this.courseRepository = courseRepository;
        this.courseMapperFactory = courseMapperFactory;
    }

    private Page<Course> getAvailableCourses(int pageNo, int pageSize) {
        // Make sure pageNo is at least 1
        pageNo = Math.max(pageNo, 1);

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<Course> coursePage = courseRepository.findByStatus(CourseStatus.AVAILABLE, pageable);

        // If the requested page number is out of bounds, return the last available page
        if (pageNo > coursePage.getTotalPages()) {
            pageable = PageRequest.of(Math.max(coursePage.getTotalPages() - 1, 0), pageSize);
            return courseRepository.findByStatus(CourseStatus.AVAILABLE, pageable);
        }

        return coursePage;
    }

    @Override
    public CoursePageDTO getAvailableCoursesPage(int pageNo, int pageSize) {

        DTOMapper<CourseOverviewDTO, Course> courseMapper = (DTOMapper<CourseOverviewDTO, Course>) courseMapperFactory.getDTOMapper(CourseOverviewDTO.class).orElse(null);

        Page<Course> coursePage = getAvailableCourses(pageNo, pageSize);
        List<CourseOverviewDTO> courses = coursePage.map(courseMapper::toDTO).getContent();
        return CoursePageDTO.builder()
                .courses(courses)
                .totalPages(coursePage.getTotalPages())
                .totalElements(coursePage.getTotalElements())
                .build();
    }
}
