package com.mgmtp.cfu.controller;

import com.mgmtp.cfu.dto.AvailableCourseRequest;
import com.mgmtp.cfu.dto.CourseDto;
import com.mgmtp.cfu.dto.CourseRequest;
import com.mgmtp.cfu.dto.CourseResponse;
import com.mgmtp.cfu.service.CourseService;
import com.mgmtp.cfu.util.CoursePageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseDtoById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseDtoById(id));
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableCourses(@ModelAttribute AvailableCourseRequest request) {

        if (!CoursePageUtil.isValidPageSize(request.getPageSize())) {
            return ResponseEntity.unprocessableEntity()
                    .body("Invalid page size. Page size must be between 1" + " and " + CoursePageUtil.getMaxPageSize());
        }

        return ResponseEntity.ok(courseService.getAvailableCoursesPage(request.getPage(), request.getPageSize(), request.getSortBy()));
    }

    @PostMapping()
    public ResponseEntity<CourseResponse> createCourse(@ModelAttribute CourseRequest courseRequest) {
        try {
            CourseResponse courseResponse = courseService.createCourse(courseRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(courseResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
