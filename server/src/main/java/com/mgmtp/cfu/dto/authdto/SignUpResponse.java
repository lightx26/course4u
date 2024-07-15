package com.mgmtp.cfu.dto.authdto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpResponse {
    @JsonProperty("message")
    private String message;
    @JsonProperty("username")
    private String username;
}
