package com.mgmtp.cfu.dto;


import com.mgmtp.cfu.enums.CourseLevel;
import com.mgmtp.cfu.enums.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

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

    public static class CategoryCourseRequestDTO {
        private String label;
        private String value;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}