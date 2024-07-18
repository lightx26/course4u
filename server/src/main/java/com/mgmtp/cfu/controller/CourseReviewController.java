package com.mgmtp.cfu.controller;

import com.mgmtp.cfu.service.CourseReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CourseReviewController {

    private final CourseReviewService courseReviewService;

    @Autowired
    public CourseReviewController(CourseReviewService courseReviewService) {
        this.courseReviewService = courseReviewService;
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
