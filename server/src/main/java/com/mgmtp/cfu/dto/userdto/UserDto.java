package com.mgmtp.cfu.dto.userdto;

import com.mgmtp.cfu.enums.Gender;
import com.mgmtp.cfu.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String telephone;
    private String avatarUrl;
    private LocalDate dateOfBirth;
    private Role role;
    private Gender gender;
    private MultipartFile imageFile;
}
