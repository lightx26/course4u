package com.mgmtp.cfu.dto.registrationdto;

import com.mgmtp.cfu.enums.DurationUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegistrationEnrollDTO {
    private Long duration;
    private DurationUnit durationUnit;
}
