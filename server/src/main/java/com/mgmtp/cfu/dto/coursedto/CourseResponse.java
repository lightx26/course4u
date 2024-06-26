package com.mgmtp.cfu.dto.coursedto;

import com.mgmtp.cfu.enums.CourseLevel;
import com.mgmtp.cfu.enums.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponse {
    private Long id;
    private String name;
    private String link;
    private String platform;
    private String thumbnailUrl;
    private String teacherName;
    private CourseStatus status;
    private CourseLevel level;
    private Set<String> categories;
}