package com.mgmtp.cfu.mapper;

import com.mgmtp.cfu.dto.coursereviewdto.CourseReviewOverviewDTO;
import com.mgmtp.cfu.entity.CourseReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class CourseReviewOverviewMapper implements DTOMapper<CourseReviewOverviewDTO, CourseReview> {
    @Override
    @Mapping(target = "userFullName", source = "courseReview.user.fullName")
    @Mapping(target = "userAvatar", source = "courseReview.user.avatarUrl")
    public abstract CourseReviewOverviewDTO toDTO(CourseReview courseReview);
}
