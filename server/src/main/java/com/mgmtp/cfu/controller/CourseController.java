package com.mgmtp.cfu.controller;

import com.mgmtp.cfu.dto.AvailableCourseRequest;
import com.mgmtp.cfu.dto.CourseDto;
import com.mgmtp.cfu.enums.CoursePageSortOption;
import com.mgmtp.cfu.service.CourseService;
import com.mgmtp.cfu.util.CoursePageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getAvailableCourses(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int pageSize,
            @RequestParam(defaultValue = "CREATED_DATE") String sortBy) {

        if (!CoursePageUtil.isValidPageSize(pageSize)) {
            return ResponseEntity.unprocessableEntity()
                    .body("Invalid page size. Page size must be between 1" + " and " + CoursePageUtil.getMaxPageSize());
        }

        CoursePageSortOption sortOption = CoursePageUtil.getSortOption(sortBy);

        return ResponseEntity.ok(courseService.getAvailableCoursesPage(page, pageSize, sortOption));
    }
}
