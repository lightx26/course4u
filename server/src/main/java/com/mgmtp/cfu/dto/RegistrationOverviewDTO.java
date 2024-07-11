package com.mgmtp.cfu.dto;

import com.mgmtp.cfu.enums.DurationUnit;
import com.mgmtp.cfu.enums.RegistrationStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class RegistrationOverviewDTO {
    private Long id;
    private RegistrationStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate registerDate;
    private LocalDateTime lastUpdated;
    private String courseName;
    private Long courseId;
    private Long userId;
    private String userName;
    private String userFullname;
    private Integer duration;
    private DurationUnit durationUnit;
}