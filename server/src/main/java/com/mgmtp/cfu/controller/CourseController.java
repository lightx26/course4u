package com.mgmtp.cfu.controller;

import com.mgmtp.cfu.dto.coursedto.*;
import com.mgmtp.cfu.exception.MapperNotFoundException;
import com.mgmtp.cfu.service.CourseService;
import com.mgmtp.cfu.util.CoursePageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        try {
            Page<CourseOverviewDTO> coursePageDTO = courseService.getAvailableCoursesPage(request.getSortBy(), request.getPage(), request.getPageSize());
            return ResponseEntity.ok(coursePageDTO);
        } catch (MapperNotFoundException e) {
            return ResponseEntity.internalServerError().body(null);
        }
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
