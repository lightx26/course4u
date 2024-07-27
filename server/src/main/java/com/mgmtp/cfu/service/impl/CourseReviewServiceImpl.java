package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.coursereviewdto.CourseReviewDto;
import com.mgmtp.cfu.dto.coursereviewdto.CourseReviewOverviewDTO;
import com.mgmtp.cfu.dto.coursereviewdto.RatingsPage;
import com.mgmtp.cfu.entity.Course;
import com.mgmtp.cfu.entity.CourseReview;
import com.mgmtp.cfu.exception.CourseNotFoundException;
import com.mgmtp.cfu.exception.MapperNotFoundException;
import com.mgmtp.cfu.mapper.factory.impl.CourseReviewMapperFactory;
import com.mgmtp.cfu.repository.CourseRepository;
import com.mgmtp.cfu.repository.CourseReviewRepository;
import com.mgmtp.cfu.service.CourseReviewService;
import com.mgmtp.cfu.specification.CourseReviewSpecifications;
import com.mgmtp.cfu.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

@Service
public class CourseReviewServiceImpl implements CourseReviewService {

    private final CourseReviewRepository courseReviewRepository;
    private final CourseReviewMapperFactory courseReviewMapperFactory;
    private final CourseRepository courseRepository;

    @Autowired
    public CourseReviewServiceImpl(CourseReviewRepository courseReviewRepository, CourseReviewMapperFactory courseReviewMapperFactory, CourseRepository courseRepository) {
        this.courseReviewRepository = courseReviewRepository;
        this.courseReviewMapperFactory = courseReviewMapperFactory;
        this.courseRepository = courseRepository;
    }

    @Override
    public RatingsPage getRatingsOfCourse(Long courseId) {
        RatingsPage ratingsPage = new RatingsPage();
    
        Double avgRating = courseReviewRepository.calculateAvgRating(courseId);
        if (avgRating == null) {
            return ratingsPage;
        }
    
        ratingsPage.setAverageRating(avgRating);
    
        Map<Integer, Long> ratingsMap = ratingsPage.getDetailRatings();
    
        List<Object[]> detailsRatings = courseReviewRepository.getRatingsInCourse(courseId);
        for (Object[] obj : detailsRatings) {
            ratingsMap.put((Integer) obj[0], ((Long) obj[1]));
        }
    
        return ratingsPage;
    }

    @Override
    public Page<CourseReviewOverviewDTO> getReviewsPageOfCourse(Long courseId, Integer starFilter, int page, int size) {

        var courseReviewMapperOpt = courseReviewMapperFactory.getDTOMapper(CourseReviewOverviewDTO.class);

        if (courseReviewMapperOpt.isEmpty()) {
            throw new MapperNotFoundException("Mapper not found");
        }

        var courseReviewMapper = courseReviewMapperOpt.get();

        Page<CourseReview> reviewsPage = getReviewsOfCourse(courseId, starFilter, page, size);
        return reviewsPage.map(courseReviewMapper::toDTO);
    }

    private Page<CourseReview> getReviewsOfCourse(Long courseId, Integer starFilter, int page, int size) {
        // Make sure the page is not less than 1
        page = Math.max(1, page);

        Sort sort = Sort.by(Sort.Order.desc("createdDate"), Sort.Order.asc("id"));
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<CourseReview> reviewsPage = geReviewsOfCourseBySpec(courseId, starFilter, pageable);

        int totalPages = reviewsPage.getTotalPages();
        // Return the last page if the requested page is greater than the total pages
        if (totalPages > 0 && page > totalPages) {
            pageable = PageRequest.of(totalPages - 1, size);
            return geReviewsOfCourseBySpec(courseId, starFilter, pageable);
        }

        return reviewsPage;
    }

    @Override
    public CourseReview saveReview(CourseReviewDto courseReviewDto) {
        CourseReview courseReview = new CourseReview();
    
        courseReview.setRating(courseReviewDto.getRating());
        courseReview.setComment(courseReviewDto.getComment());
        courseReview.setCreatedDate(LocalDateTime.now());
        courseReview.setUser(AuthUtils.getCurrentUser());
    
        Course course = courseRepository.findById(courseReviewDto.getCourseId())
                                        .orElseThrow(() -> new CourseNotFoundException("Course with id '" + courseReviewDto.getCourseId() + "' not found"));
    
        courseReview.setCourse(course);
    
        return courseReviewRepository.save(courseReview);
    }

    @Override
    public boolean checkReviewed(Long courseId) {
        if (courseId == null) {
            return false;
        }

        if (courseRepository.findById(courseId).isEmpty()) {
            throw new CourseNotFoundException("Course with id '" + courseId + "' not found");
        }
        Long userId = AuthUtils.getCurrentUser().getId();
        return courseReviewRepository.existsByCourseIdAndUserId(courseId, userId);
    }

    private Page<CourseReview> geReviewsOfCourseBySpec(Long courseId, Integer starFilter, Pageable pageable) {
        Specification<CourseReview> spec = CourseReviewSpecifications.hasCourseId(courseId);

        if (starFilter != null) {
            spec = spec.and(CourseReviewSpecifications.getRatingSpec(starFilter));
        }

        return courseReviewRepository.findAll(spec, pageable);
    }

}