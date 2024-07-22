package com.mgmtp.cfu.controller;

import com.mgmtp.cfu.dto.coursereviewdto.CourseReviewDto;
import com.mgmtp.cfu.entity.CourseReview;
import com.mgmtp.cfu.service.CourseReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CourseReviewController {

    private final CourseReviewService courseReviewService;

    @PostMapping("/submit-review")
    public ResponseEntity<CourseReview> sendReview(@Valid @RequestBody CourseReviewDto courseReviewDto) {
        CourseReview savedCourseReview = courseReviewService.saveReview(courseReviewDto);
        return ResponseEntity.ok(savedCourseReview);
    }

    @GetMapping("/courses/{courseId}/ratings")
    public ResponseEntity<?> getRatingsOfCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseReviewService.getRatingsOfCourse(courseId));
    }

    @GetMapping("/courses/{courseId}/reviews")
    public ResponseEntity<?> getReviewsOfCourse(@PathVariable Long courseId,
                                                @RequestParam(defaultValue = "0") Integer starFilter,
                                                @RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "3") int size) {
        return ResponseEntity.ok(courseReviewService.getReviewsPageOfCourse(courseId, starFilter, page, size));
    }

}
