package com.mgmtp.cfu.dto.coursedto;

import com.mgmtp.cfu.enums.CourseLevel;
import com.mgmtp.cfu.enums.CourseStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
