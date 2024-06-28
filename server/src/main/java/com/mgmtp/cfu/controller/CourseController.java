package com.mgmtp.cfu.controller;

import com.mgmtp.cfu.dto.CourseDto;
import com.mgmtp.cfu.service.CourseService;
import com.mgmtp.cfu.util.CoursePageValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseDtoById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseDtoById(id));
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableCourses(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "${course.page.default-size}") int pageSize) {

        if (!CoursePageValidator.isValidPageSize(pageSize)) {
            return ResponseEntity.unprocessableEntity()
                    .body("Invalid page size. Page size must be between 1" + " and " + CoursePageValidator.getMaxPageSize());
        }

        return ResponseEntity.ok(courseService.getAvailableCoursesPage(page, pageSize));
    }

}
