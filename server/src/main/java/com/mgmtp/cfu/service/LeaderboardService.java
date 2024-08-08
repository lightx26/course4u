package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.leaderboarddto.LeaderboardDTO;

import java.util.Set;

public interface LeaderboardService {
    LeaderboardDTO getLeaderboard(int year);

    LeaderboardDTO getLeaderboardWiki(int year);

    Set<String> getExistedYears();
}
