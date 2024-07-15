package com.mgmtp.cfu.dto.userdto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mgmtp.cfu.enums.Gender;
import com.mgmtp.cfu.enums.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level= AccessLevel.PRIVATE)
public class UserRegistrationDTO {
    Long id;
    String username;
    String fullName;
    String email;
    String telephone;
    String avatarUrl;
    LocalDate dateOfBirth;
    Role role;
    Gender gender;
}
