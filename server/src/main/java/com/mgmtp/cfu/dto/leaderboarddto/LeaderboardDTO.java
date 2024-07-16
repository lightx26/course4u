package com.mgmtp.cfu.dto.leaderboarddto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeaderboardDTO {
    private int year;
    private List<LeaderboardUserDTO> leaderboardUserDTOs;

}
