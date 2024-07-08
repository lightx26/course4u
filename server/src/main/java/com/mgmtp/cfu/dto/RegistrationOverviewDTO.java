package com.mgmtp.cfu.dto;

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
    Long id;
    RegistrationStatus status;
    LocalDate startDate;
    LocalDate registerDate;
    LocalDate endDate;
    String courseName;
    LocalDateTime lastUpdate;
    Long courseId;
    Long userId;
    String username;
    String userFullName;
    String thumbnailUrl;

}
