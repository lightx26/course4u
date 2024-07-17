package com.mgmtp.cfu.dto.coursedto;

import com.mgmtp.cfu.entity.Category;
import com.mgmtp.cfu.enums.CourseLevel;
import com.mgmtp.cfu.enums.CoursePlatform;
import com.mgmtp.cfu.enums.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CourseDto {

    private Long id;

    private String name;

    private String link;

    private CoursePlatform platform;

    private String thumbnailUrl;

    private String teacherName;

    private LocalDate createdDate;

    private CourseStatus status;

    private CourseLevel level;

    private Set<Category> categories;

}
