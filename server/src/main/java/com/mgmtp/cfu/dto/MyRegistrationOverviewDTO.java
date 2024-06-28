package com.mgmtp.cfu.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mgmtp.cfu.entity.Course;
import com.mgmtp.cfu.entity.User;
import com.mgmtp.cfu.enums.DurationUnit;
import com.mgmtp.cfu.enums.RegistrationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
public class MyRegistrationOverviewDTO {
    Long id;
    RegistrationStatus status;
    LocalDate startDate;
    LocalDate registerDate;
    LocalDate endDate;
    String courseName;
    LocalDateTime lastUpdate;
    Long courseId;
}
