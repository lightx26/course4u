package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.coursedto.*;

import com.mgmtp.cfu.dto.coursedto.CourseRequest;
import com.mgmtp.cfu.dto.coursedto.CourseResponse;
import com.mgmtp.cfu.entity.Category;
import com.mgmtp.cfu.entity.Course;
import com.mgmtp.cfu.exception.CourseNotFoundException;
import com.mgmtp.cfu.enums.CoursePageSortOption;
import com.mgmtp.cfu.enums.CourseStatus;
import com.mgmtp.cfu.exception.DuplicateCourseException;
import com.mgmtp.cfu.exception.MapperNotFoundException;
import com.mgmtp.cfu.mapper.DTOMapper;
import com.mgmtp.cfu.mapper.factory.MapperFactory;
import com.mgmtp.cfu.repository.CourseRepository;
import com.mgmtp.cfu.service.CategoryService;
import com.mgmtp.cfu.service.CourseService;
import com.mgmtp.cfu.service.UploadService;
import com.mgmtp.cfu.specification.CourseSpecifications;
import com.mgmtp.cfu.util.RegistrationStatusUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Set;

@Service
@Log4j2
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final MapperFactory<Course> courseMapperFactory;
    private final CategoryService categoryService;
    private final UploadService uploadService;
    @Value("${course4u.upload.thumbnail-directory}")
    private String uploadThumbnailDir;
    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, MapperFactory<Course> courseMapperFactory, CategoryService categoryService, UploadService uploadService) {
        this.courseRepository = courseRepository;
        this.courseMapperFactory = courseMapperFactory;
        this.categoryService = categoryService;
        this.uploadService = uploadService;
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
    public CourseResponse createCourse(CourseRequest courseRequest) {
        var modelMapper = new ModelMapper();
        try {
            Course course = modelMapper.map(courseRequest, Course.class);
            if (courseRepository.findFirstByLinkIgnoreCase(course.getLink()) != null) {
                throw new DuplicateCourseException("Course with link " + course.getLink() + " already exists");
            }
            String thumbnailUrl = null;
            if (courseRequest.getThumbnailFile() != null)
            {
                thumbnailUrl = uploadService.uploadThumbnail(courseRequest.getThumbnailFile(), uploadThumbnailDir);
            } else if (courseRequest.getThumbnailUrl() != null) {
                thumbnailUrl = courseRequest.getThumbnailUrl();
            }
//            Value receive from client is category id
            List<Category> categories = categoryService.findCategoriesByIds(
                    courseRequest.getCategories()
                            .stream()
                            .map(category -> Long.parseLong(category.getValue()))
                            .toList()
            );
            course.setThumbnailUrl(thumbnailUrl);
            course.setCategories(Set.copyOf(categories));
            course.setCreatedDate(LocalDate.now());
            course.setStatus(CourseStatus.AVAILABLE);
            course = courseRepository.save(course);
            return modelMapper.map(course, CourseResponse.class);
        } catch (IOException e) {
            log.error("Error while uploading thumbnail", e);
            throw new RuntimeException("Error while uploading thumbnail");
        }
    }

    @Override
    public Page<CourseOverviewDTO> getAvailableCoursesPage(CoursePageFilter filter, CoursePageSortOption sortBy, int pageNo, int pageSize) {
        Optional<DTOMapper<CourseOverviewDTO, Course>> courseMapperOpt = courseMapperFactory.getDTOMapper(CourseOverviewDTO.class);

        if (courseMapperOpt.isEmpty()) {
            throw new MapperNotFoundException("No mapper found for CourseOverviewDTO");
        }

        DTOMapper<CourseOverviewDTO, Course> courseMapper = courseMapperOpt.get();

        Page<Course> coursePage = getAvailableCourses(filter, sortBy, pageNo, pageSize);
        return coursePage.map(courseMapper::toDTO);
    }

    private Page<Course> getAvailableCourses(CoursePageFilter filter, CoursePageSortOption sortBy, int pageNo, int pageSize) {
        // Make sure pageNo is valid: at least 1 and at most maxPageNum
        pageNo = Math.max(pageNo, 1);

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Page<Course> coursePage = getAvailableCourseBySpec(filter, sortBy, pageable);

        // If pageNo is greater than the total number of pages, return the last page
        int totalPages = coursePage.getTotalPages();
        if (totalPages > 0 && pageNo > totalPages) {
            pageable = PageRequest.of(totalPages - 1, pageSize);
            return getAvailableCourseBySpec(filter, sortBy, pageable);
        }

        return coursePage;
    }

    private Page<Course> getAvailableCourseBySpec(CoursePageFilter filter, CoursePageSortOption sortBy, Pageable pageable) {
        Specification<Course> spec = CourseSpecifications.hasStatus(CourseStatus.AVAILABLE);

        if (filter != null) {
            spec = spec.and(getFilterSpec(filter));
        }

        if (sortBy != null) {
            spec = spec.and(getSortSpec(sortBy));
        }

        return courseRepository.findAll(spec, pageable);
    }

    private Specification<Course> getFilterSpec(CoursePageFilter filter) {
        Specification<Course> spec = Specification.where(null);

        if (!filter.getCategoryFilters().isEmpty()) {
            spec = spec.and(CourseSpecifications.hasCategories(filter.getCategoryFilters()));
        }

        if (filter.getMinRating() > 0) {
            spec = spec.and(CourseSpecifications.hasRatingGreaterThan(filter.getMinRating()));
        }

        if (!filter.getLevelFilters().isEmpty()) {
            spec = spec.and(CourseSpecifications.hasLevels(filter.getLevelFilters()));
        }

        if (!filter.getPlatformFilters().isEmpty()) {
            spec = spec.and(CourseSpecifications.hasPlatforms(filter.getPlatformFilters()));
        }

        return spec;
    }

    private Specification<Course> getSortSpec(CoursePageSortOption sortBy) {
        return switch (sortBy) {
            case NEWEST -> CourseSpecifications.sortByCreatedDateDesc();
            case MOST_ENROLLED -> CourseSpecifications.sortByEnrollmentCountDesc(RegistrationStatusUtil.ACCEPTED_STATUSES);
            case RATING -> CourseSpecifications.sortByRatingDesc();
            default -> Specification.where(null);
        };
    }
}
