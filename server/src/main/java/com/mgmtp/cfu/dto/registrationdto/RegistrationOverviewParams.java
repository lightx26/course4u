package com.mgmtp.cfu.dto.registrationdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class RegistrationOverviewParams {
    private String status = "all";
    private String search = "";
    private String orderBy = "";
    private Boolean isAscending = Boolean.TRUE;
}
