package com.mgmtp.cfu.dto.coursereviewdto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CourseReviewDto {

    @NotNull(message = "Rating cannot be null")
    private Integer rating;

    private String comment;

    private Long courseId;

}
