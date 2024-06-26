package com.mgmtp.cfu.dto;

import com.mgmtp.cfu.enums.*;
import com.mgmtp.cfu.util.UrlUtils;
import lombok.*;
import com.mgmtp.cfu.dto.coursedto.CourseRequest;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationRequest {
    private String name;
    private String link;
    private CoursePlatform platform;
    private MultipartFile thumbnailFile;
    private String thumbnailUrl;
    private String teacherName;
    private List<CourseRequest.CategoryCourseRequestDTO> categories;
    private CourseLevel level;
    private Integer duration;
    private DurationUnit durationUnit;
    private LocalDateTime lastUpdate;
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
