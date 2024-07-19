package com.mgmtp.cfu.dto.registrationdto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mgmtp.cfu.dto.userdto.UserDto;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationFeedbackDTO {
    private Long id;
    private String comment;
    private LocalDateTime createdDate;
    private UserDto user;
}
