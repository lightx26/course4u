package com.mgmtp.cfu.dto;

import com.mgmtp.cfu.enums.CourseLevel;
import com.mgmtp.cfu.enums.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CourseOverviewDTO {
    private Long id;
    private String name;
    private String platform;
    private CourseLevel level;
    private String thumbnailUrl;
    // TODO: Implement these fields when we have the data
    //    private Double rating;
    private Integer enrollmentCount;
    private CourseStatus status;
}
