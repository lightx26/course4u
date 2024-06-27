package com.mgmtp.cfu.controller;


import com.mgmtp.cfu.dto.CourseDTO;
import com.mgmtp.cfu.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/courses")
public class CourseController {
    private final CourseService courseService;

    @Value("${course.page.size}")
    private int COURSE_PAGE_SIZE;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/available")
    public ResponseEntity<List<CourseDTO>> getAvailableCourses(@RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(courseService.getAvailableCoursesPage(page, COURSE_PAGE_SIZE));
    }

    @GetMapping("/available/page-count")
    public ResponseEntity<Integer> getAvailableCoursesPageCount() {
        return ResponseEntity.ok(courseService.getAvailableCoursesPageCount(COURSE_PAGE_SIZE));
    }
}
