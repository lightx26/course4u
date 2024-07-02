package com.mgmtp.cfu.dto;

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
    private long totalElements;
}
