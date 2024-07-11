package com.mgmtp.cfu.util;

import com.mgmtp.cfu.enums.CoursePageSortOption;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CoursePageUtil {

    @Getter
    private static int maxPageSize = 32;

    @Value("${course.page.max-size}")
    private void setMaxPageSize(int maxPageSize) {
        CoursePageUtil.maxPageSize = maxPageSize;
    }

    public static boolean isValidPageSize(int pageSize) {
        return pageSize >= 1 && pageSize <= maxPageSize;
    }

    public static CoursePageSortOption getSortOption(String sortBy) {
        sortBy = sortBy.toUpperCase();
        try {
            return CoursePageSortOption.valueOf(sortBy);
        } catch (IllegalArgumentException e) {
            return CoursePageSortOption.CREATED_DATE;
        }
    }
}