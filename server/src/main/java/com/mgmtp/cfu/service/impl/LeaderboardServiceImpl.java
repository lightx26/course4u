package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.leaderboarddto.LeaderboardDTO;
import com.mgmtp.cfu.repository.queries.LeaderboardQueryManager;
import com.mgmtp.cfu.service.LeaderboardService;
import com.mgmtp.cfu.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.Set;

import static com.mgmtp.cfu.util.Constant.LEADERBOARD_RANK_LIMIT;


@Service
@RequiredArgsConstructor
@Slf4j
public class LeaderboardServiceImpl implements LeaderboardService {
    private final LeaderboardQueryManager leaderboardQueryManager;

    @Override
    public LeaderboardDTO getLeaderboard(int year) {
        var listOfLeaderboardUser = leaderboardQueryManager.getLeaderboardUsers(year, LEADERBOARD_RANK_LIMIT);
        var currentUserId = AuthUtils.getCurrentUser().getId();
        listOfLeaderboardUser.stream()
                .filter(leaderboardUserDTO -> leaderboardUserDTO.getUserId().equals(currentUserId))
                .findFirst()
                .ifPresent(leaderboardUserDTO -> leaderboardUserDTO.setMine(true));
        return LeaderboardDTO.builder()
                .leaderboardUserDTOs(listOfLeaderboardUser)
                .year(year)
                .build();
    }

    @Override
    public Set<String> getExistedYears() {
        return leaderboardQueryManager.getExistedYears();
    }
}
