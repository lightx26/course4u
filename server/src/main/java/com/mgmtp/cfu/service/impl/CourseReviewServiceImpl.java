package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.coursereviewdto.CourseReviewOverviewDTO;
import com.mgmtp.cfu.dto.coursereviewdto.RatingsPage;
import com.mgmtp.cfu.entity.CourseReview;
import com.mgmtp.cfu.exception.MapperNotFoundException;
import com.mgmtp.cfu.mapper.factory.impl.CourseReviewMapperFactory;
import com.mgmtp.cfu.repository.CourseReviewRepository;
import com.mgmtp.cfu.service.CourseReviewService;
import com.mgmtp.cfu.specification.CourseReviewSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CourseReviewServiceImpl implements CourseReviewService {

    private final CourseReviewRepository courseReviewRepository;
    private final CourseReviewMapperFactory courseReviewMapperFactory;

    @Autowired
    public CourseReviewServiceImpl(CourseReviewRepository courseReviewRepository, CourseReviewMapperFactory courseReviewMapperFactory) {
        this.courseReviewRepository = courseReviewRepository;
        this.courseReviewMapperFactory = courseReviewMapperFactory;
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

    private Page<CourseReview> geReviewsOfCourseBySpec(Long courseId, Integer starFilter, Pageable pageable) {
        Specification<CourseReview> spec = CourseReviewSpecifications.hasCourseId(courseId);

        if (starFilter != null) {
            spec = spec.and(CourseReviewSpecifications.getRatingSpec(starFilter));
        }

        return courseReviewRepository.findAll(spec, pageable);
    }
}
