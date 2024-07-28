package com.mgmtp.cfu.dto.coursedto;

import com.mgmtp.cfu.dto.categorydto.CategoryDTO;
import com.mgmtp.cfu.enums.CourseLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoursePageFilter {
    // List of category ids
    private List<Long> categoryFilters = new ArrayList<>();
    // Rating
    private List<Integer> ratingFilters = new ArrayList<>();
    // List of levels
    private List<CourseLevel> levelFilters = new ArrayList<>();
    // List of platforms
    private List<String> platformFilters = new ArrayList<>();
}
