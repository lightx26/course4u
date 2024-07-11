package com.mgmtp.cfu.dto;

import com.mgmtp.cfu.enums.CoursePageSortOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvailableCourseRequest {
    private int page = 1;
    private int pageSize = 8;
    private CoursePageSortOption sortBy = CoursePageSortOption.CREATED_DATE;
}
