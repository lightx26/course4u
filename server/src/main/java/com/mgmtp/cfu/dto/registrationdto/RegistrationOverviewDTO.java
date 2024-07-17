package com.mgmtp.cfu.dto.registrationdto;

import com.mgmtp.cfu.enums.DurationUnit;
import com.mgmtp.cfu.enums.RegistrationStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
public class RegistrationOverviewDTO {
    private Long id;
    private RegistrationStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate registerDate;
    private LocalDateTime lastUpdated;
    private Long courseId;
    private String courseName;
    private String courseThumbnailUrl;
    private String coursePlatform;
    private Long userId;
    private String userName;
    private String userFullname;
    private String userAvatarUrl;
    private Integer duration;
    private DurationUnit durationUnit;
}
