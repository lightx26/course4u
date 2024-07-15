package com.mgmtp.cfu.dto.authdto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignUpRequest {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String fullname;
    private String dateofbirth;
    private String gender;
}
