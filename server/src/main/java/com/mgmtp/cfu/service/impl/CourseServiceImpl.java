package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.CourseDto;
import com.mgmtp.cfu.dto.CourseOverviewDTO;

import com.mgmtp.cfu.dto.CoursePageDTO;
import com.mgmtp.cfu.entity.Course;
import com.mgmtp.cfu.exception.CourseNotFoundException;
import com.mgmtp.cfu.enums.CoursePageSortOption;
import com.mgmtp.cfu.enums.CourseStatus;
import com.mgmtp.cfu.mapper.DTOMapper;
import com.mgmtp.cfu.mapper.factory.MapperFactory;
import com.mgmtp.cfu.repository.CourseRepository;
import com.mgmtp.cfu.service.CourseService;
import com.mgmtp.cfu.specification.CourseSpecifications;
import com.mgmtp.cfu.util.RegistrationStatusUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final MapperFactory<Course> courseMapperFactory;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, MapperFactory<Course> courseMapperFactory) {
        this.courseRepository = courseRepository;
        this.courseMapperFactory = courseMapperFactory;
    }

    @Override
    public CourseDto getCourseDtoById(Long id) {
        Optional<Course> optCourse = courseRepository.findById(id);
        if (optCourse.isPresent()) {
            Course course = optCourse.get();
            return new CourseDto(course.getId(), course.getName(), course.getLink(), course.getPlatform(), course.getThumbnailUrl(), course.getTeacherName(),
                                 course.getCreatedDate(), course.getStatus(), course.getLevel(), course.getCategories());
        }
        throw new CourseNotFoundException("course with id " + id + " not found");
    }

    @Override
    public CoursePageDTO getAvailableCoursesPage(int pageNo, int pageSize, CoursePageSortOption sortBy) {
        Optional<DTOMapper<CourseOverviewDTO, Course>> courseMapperOpt = courseMapperFactory.getDTOMapper(CourseOverviewDTO.class);

        if (courseMapperOpt.isEmpty()) {
            throw new IllegalStateException("No mapper found for CourseOverviewDTO");
        }

        DTOMapper<CourseOverviewDTO, Course> courseMapper = courseMapperOpt.get();

        Page<Course> coursePage = getAvailableCourses(pageNo, pageSize, sortBy);
        List<CourseOverviewDTO> courses = coursePage.map(courseMapper::toDTO).getContent();
        return CoursePageDTO.builder()
                .courses(courses)
                .totalPages(coursePage.getTotalPages())
                .totalElements((int) coursePage.getTotalElements())
                .build();
    }

    private Page<Course> getAvailableCourses(int pageNo, int pageSize, CoursePageSortOption sortBy) {
        // Make sure pageNo is valid: at least 1 and at most maxPageNum
        pageNo = Math.max(pageNo, 1);

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Specification<Course> spec = CourseSpecifications.hasStatus(CourseStatus.AVAILABLE);

        Page<Course> coursePage = getAvailableCourseBySpec(spec, pageable, sortBy);

        // If pageNo is greater than the total number of pages, return the last page
        int totalPages = coursePage.getTotalPages();
        if (pageNo > totalPages) {
            pageable = PageRequest.of(totalPages - 1, pageSize);
            return getAvailableCourseBySpec(spec, pageable, sortBy);
        }

        return coursePage;
    }

    private Page<Course> getAvailableCourseBySpec(Specification<Course> spec, Pageable pageable, CoursePageSortOption sortBy) {

        Specification<Course> sortSpec = switch (sortBy) {
            case CREATED_DATE -> spec.and(CourseSpecifications.sortByCreatedDateDesc());
            case ENROLLMENTS ->
                    spec.and(CourseSpecifications.sortByEnrollmentCountDesc(RegistrationStatusUtil.ACCEPTED_STATUSES));
            case RATING -> spec.and(CourseSpecifications.sortByRatingDesc());
            default -> spec.and(CourseSpecifications.sortByCreatedDateDesc());
        };

        return courseRepository.findAll(sortSpec, pageable);
    }
}
