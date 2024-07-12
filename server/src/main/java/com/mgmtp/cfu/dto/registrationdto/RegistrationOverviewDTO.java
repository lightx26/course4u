package com.mgmtp.cfu.dto.registrationdto;

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
    private LocalDate registerDate;
    private LocalDate endDate;
    private String courseName;
    private LocalDateTime lastUpdate;
    private Long courseId;
    private Long userId;
    private String userName;
    private String userFullName;
    private String thumbnailUrl;
    private String platform;
}
