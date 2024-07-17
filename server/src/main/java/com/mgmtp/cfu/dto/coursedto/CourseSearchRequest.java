package com.mgmtp.cfu.dto.coursedto;

import com.mgmtp.cfu.enums.CoursePageSortOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseSearchRequest {
    private int page = 1;
    private int pageSize = 8;
    private String search = "";
    private CoursePageFilter filter = new CoursePageFilter();
    private CoursePageSortOption sortBy = CoursePageSortOption.NEWEST;
}
