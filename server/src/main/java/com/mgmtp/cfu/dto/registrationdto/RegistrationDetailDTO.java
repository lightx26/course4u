package com.mgmtp.cfu.dto.registrationdto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mgmtp.cfu.dto.coursedto.CourseRegistrationDTO;
import com.mgmtp.cfu.dto.userdto.UserRegistrationDTO;
import com.mgmtp.cfu.enums.DurationUnit;
import com.mgmtp.cfu.enums.RegistrationStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationDetailDTO {
    Long id;
    RegistrationStatus status;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Integer score;
    LocalDate registerDate;
    Integer duration;
    DurationUnit durationUnit;
    CourseRegistrationDTO course;
    UserRegistrationDTO user;
    Set<RegistrationFeedbackDTO> registrationFeedbacks;
}