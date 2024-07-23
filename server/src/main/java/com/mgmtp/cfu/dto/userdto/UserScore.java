package com.mgmtp.cfu.dto.userdto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserScore {
    private int rank;
    private long score;
    private String year;
}
