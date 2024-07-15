package com.mgmtp.cfu.dto.coursedto;


import com.mgmtp.cfu.enums.CourseLevel;
import com.mgmtp.cfu.enums.CourseStatus;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRequest {
    private String name;
    private String link;
    private String platform;
    private MultipartFile thumbnailFile;
    private String thumbnailUrl;
    private String teacherName;
    private CourseStatus status;
    private CourseLevel level;
    private List<CategoryCourseRequestDTO> categories;

    @Getter
    @Setter
    public static class CategoryCourseRequestDTO {
        private String label;
        private String value;
    }
}