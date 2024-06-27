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
public class CourseDTO {
    private String name;
    private String link;
    private String platform;
    private CourseLevel level;
    private String thumbnailUrl;
    private String teacherName;
    private LocalDate createdDate;
    private CourseStatus status;
}
