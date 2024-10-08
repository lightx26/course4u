package com.mgmtp.cfu.dto.coursedto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoursePageDTO {
    private List<CourseOverviewDTO> courses;
    private int totalPages;
    private int totalElements;
}
