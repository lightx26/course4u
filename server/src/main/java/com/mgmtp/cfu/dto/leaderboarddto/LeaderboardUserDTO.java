package com.mgmtp.cfu.dto.leaderboarddto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LeaderboardUserDTO {
    private Long userId;
    private int score;
    private int learningTime;
    private boolean isMine;
    private String avatarUrl;
    private String email;
    private String username;
    private String fullName;

    public LeaderboardUserDTO(Long userId, Long score, Long learningTime, String avatarUrl, String email, String username, String fullName) {
        this.userId = userId;
        this.score = score.intValue();
        this.learningTime = learningTime.intValue();
        this.avatarUrl = avatarUrl;
        this.email = email;
        this.username = username;
        this.fullName = fullName;
    }
    public LeaderboardUserDTO(Long userId,String avatarUrl, String email, String username, String fullName) {
        this.userId = userId;
        this.avatarUrl = avatarUrl;
        this.email = email;
        this.username = username;
        this.fullName = fullName;
    }


}
