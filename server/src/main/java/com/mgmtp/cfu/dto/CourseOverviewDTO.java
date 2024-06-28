package com.mgmtp.cfu.dto;

import com.mgmtp.cfu.enums.CourseLevel;
import com.mgmtp.cfu.enums.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CourseOverviewDTO {
    private Long id;
    private String name;
    private String platform;
    private CourseLevel level;
    private LocalDate createdDate;
    private String thumbnailUrl;
    private Double rating;
    private Integer enrollmentCount;
    private CourseStatus status;
}
