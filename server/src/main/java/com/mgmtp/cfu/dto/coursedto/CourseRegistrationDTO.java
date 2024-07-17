package com.mgmtp.cfu.dto.coursedto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mgmtp.cfu.dto.categorydto.CategoryRegistrationDTO;
import com.mgmtp.cfu.enums.CourseLevel;
import com.mgmtp.cfu.enums.CoursePlatform;
import com.mgmtp.cfu.enums.CourseStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseRegistrationDTO {
    private Long id;
    private String name;
    private String link;
    private CoursePlatform platform;
    private String thumbnailUrl;
    private String teacherName;
    private LocalDate createdDate;
    private CourseStatus status;
    private CourseLevel level;
    private List<CategoryRegistrationDTO> categories;
}
