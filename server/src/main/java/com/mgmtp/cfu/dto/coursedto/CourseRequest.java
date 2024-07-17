package com.mgmtp.cfu.dto.coursedto;

import com.mgmtp.cfu.enums.CourseLevel;
import com.mgmtp.cfu.enums.CoursePlatform;
import com.mgmtp.cfu.enums.CourseStatus;
import com.mgmtp.cfu.util.UrlUtils;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRequest {
    private String name;
    private String link;
    private CoursePlatform platform;
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


    public void setName(String name) {
        this.name = name != null ? name.trim() : null;
    }

    public void setLink(String link) throws MalformedURLException {
        this.link = link != null ? UrlUtils.standardizeUrl(link.trim()) : null;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName != null ? teacherName.trim() : null;
    }
}
