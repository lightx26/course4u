package com.mgmtp.cfu.mapper;

import com.mgmtp.cfu.dto.CourseDTO;
import com.mgmtp.cfu.entity.Course;

import java.util.List;

public class CourseMapper {
    public static CourseDTO toDTO(Course course) {
        if (course == null) {
            return null;
        }

        return CourseDTO.builder()
                .name(course.getName())
                .link(course.getLink())
                .platform(course.getPlatform())
                .level(course.getLevel())
                .thumbnailUrl(course.getThumbnailUrl())
                .teacherName(course.getTeacherName())
                .createdDate(course.getCreatedDate())
                .status(course.getStatus())
                .build();
    }

    public static List<CourseDTO> toDTO(List<Course> courses) {
        return courses.stream().map(CourseMapper::toDTO).toList();
    }

    public static Course toEntity(CourseDTO courseDTO) {
        return Course.builder()
                .name(courseDTO.getName())
                .link(courseDTO.getLink())
                .platform(courseDTO.getPlatform())
                .level(courseDTO.getLevel())
                .thumbnailUrl(courseDTO.getThumbnailUrl())
                .teacherName(courseDTO.getTeacherName())
                .createdDate(courseDTO.getCreatedDate())
                .status(courseDTO.getStatus())
                .build();
    }

    public static List<Course> toEntity(List<CourseDTO> courseDTOs) {
        return courseDTOs.stream().map(CourseMapper::toEntity).toList();
    }
}
