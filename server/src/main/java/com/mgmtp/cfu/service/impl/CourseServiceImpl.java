package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.CourseDto;
import com.mgmtp.cfu.dto.CourseOverviewDTO;

import com.mgmtp.cfu.dto.CoursePageDTO;
import com.mgmtp.cfu.dto.CourseRequest;
import com.mgmtp.cfu.dto.CourseResponse;
import com.mgmtp.cfu.entity.Category;
import com.mgmtp.cfu.entity.Course;
import com.mgmtp.cfu.exception.CourseNotFoundException;
import com.mgmtp.cfu.enums.CoursePageSortOption;
import com.mgmtp.cfu.enums.CourseStatus;
import com.mgmtp.cfu.mapper.DTOMapper;
import com.mgmtp.cfu.mapper.factory.MapperFactory;
import com.mgmtp.cfu.repository.CategoryRepository;
import com.mgmtp.cfu.repository.CategoryRepository;
import com.mgmtp.cfu.repository.CourseRepository;
import com.mgmtp.cfu.service.CourseService;
import com.mgmtp.cfu.specification.CourseSpecifications;
import com.mgmtp.cfu.util.RegistrationStatusUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final MapperFactory<Course> courseMapperFactory;
    private final CategoryRepository categoryRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, MapperFactory<Course> courseMapperFactory, CategoryRepository categoryRepository) {
        this.courseRepository = courseRepository;
        this.courseMapperFactory = courseMapperFactory;
        this.categoryRepository = categoryRepository;
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

//    private final ModelMapper modelMapper;
    @Value("${uploadDir}")
    private String uploadDir;
    @Override
    public CourseResponse createCourse(CourseRequest courseRequest) {
        var modelMapper = new ModelMapper();
        try {
            Course course = modelMapper.map(courseRequest, Course.class);
            System.out.println(course.toString());
            // Generate a UUID for the thumbnail filename
            String uuidFilename = UUID.randomUUID().toString() + ".jpg"; // Default extension
            String thumbnailUrl = uuidFilename;

            // Handle thumbnail file upload if present
            MultipartFile thumbnailFile = courseRequest.getThumbnailFile();
            if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
                Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                String originalFilename = thumbnailFile.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                uuidFilename = UUID.randomUUID().toString() + fileExtension; // Update UUID filename with correct extension
                Path filePath = uploadPath.resolve(uuidFilename);
                thumbnailFile.transferTo(filePath.toFile());
                thumbnailUrl = uuidFilename; // Update URL with the actual uploaded file name
            } else if (thumbnailUrl != null) {
                thumbnailUrl = courseRequest.getThumbnailUrl();
            }

            course.setThumbnailUrl(thumbnailUrl);
            Set<Category> categories = new HashSet<>();
            if (courseRequest.getCategories() != null && !courseRequest.getCategories().isEmpty()) {
                for (CourseRequest.CategoryCourseRequestDTO categoryDTO : courseRequest.getCategories()) {
                    String categoryName = categoryDTO.getValue();
                    Category category = categoryRepository.findByName(categoryName);
                    if (category == null) {

                    }
                    categories.add(category);
                }
            }

            // Set categories in course entity
            course.setCategories(categories);
            course = courseRepository.save(course);
            return modelMapper.map(course, CourseResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
