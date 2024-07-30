package com.mgmtp.cfu.dto.coursedto;

import com.mgmtp.cfu.dto.categorydto.CategoryDTO;
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

    private String platform;

    private String thumbnailUrl;

    private String teacherName;

    private LocalDate createdDate;

    private CourseStatus status;

    private CourseLevel level;

    private Set<CategoryDTO> categories;

    private Long totalEnrollees;


    public CourseDto(Long id, String name, String link, CoursePlatform platform, String thumbnailUrl,
                     String teacherName, LocalDate createdDate, CourseStatus status,
                     CourseLevel level, Long totalEnrollees) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.platform = platform.name();
        this.thumbnailUrl = thumbnailUrl;
        this.teacherName = teacherName;
        this.createdDate = createdDate;
        this.status = status;
        this.level = level;
        this.totalEnrollees = totalEnrollees;
    }
}
