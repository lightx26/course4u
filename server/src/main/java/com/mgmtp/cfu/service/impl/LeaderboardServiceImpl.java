package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.leaderboarddto.LeaderboardDTO;
import com.mgmtp.cfu.dto.leaderboarddto.LeaderboardUserDTO;
import com.mgmtp.cfu.repository.queries.ScoreQueryManager;
import com.mgmtp.cfu.service.LeaderboardService;
import com.mgmtp.cfu.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.mgmtp.cfu.util.Constant.LEADERBOARD_RANK_LIMIT;


@Service
@RequiredArgsConstructor
@Slf4j
public class LeaderboardServiceImpl implements LeaderboardService {
    private final ScoreQueryManager leaderboardQueryManager;

    @Override
    public LeaderboardDTO getLeaderboard(int year) {
        var originalList = leaderboardQueryManager.getLeaderboardUsers(year, 20);
        // Create a modifiable copy of the list
        var listOfLeaderboardUser = new ArrayList<>(originalList);

        // Get the current user's ID
        var currentUserId = AuthUtils.getCurrentUser().getId();

        listOfLeaderboardUser.stream()
                .filter(leaderboardUserDTO -> leaderboardUserDTO.getUserId().equals(currentUserId))
                .findFirst()
                .ifPresent(leaderboardUserDTO -> leaderboardUserDTO.setMine(true));

        return LeaderboardDTO.builder()
                .leaderboardUserDTOs(listOfLeaderboardUser.stream()
                        .sorted(Comparator.comparingInt(LeaderboardUserDTO::getScore).reversed() // Score in descending order
                                .thenComparingInt(LeaderboardUserDTO::getLearningTime)
                                .thenComparing((o1, o2) -> {
                                    if (o1.getFullName().isEmpty()) {
                                        return 1;
                                    } else if (o2.getFullName().isEmpty()) {
                                        return -1;
                                    } else {
                                        return o1.getFullName().compareToIgnoreCase(o2.getFullName());
                                    }
                                })
                        )

                        .toList().subList(0,Math.min(LEADERBOARD_RANK_LIMIT, listOfLeaderboardUser.size())))
                .year(year)
                .build();

    }

    @Override
    public LeaderboardDTO getLeaderboardWiki(int year) {
        var originalList = leaderboardQueryManager.getLeaderboardUsers(year, 20);
        // Create a modifiable copy of the list
        var listOfLeaderboardUser = new ArrayList<>(originalList);
        return LeaderboardDTO.builder()
                .leaderboardUserDTOs(listOfLeaderboardUser.stream()
                        .sorted(Comparator.comparingInt(LeaderboardUserDTO::getScore).reversed() // Score in descending order
                                .thenComparingInt(LeaderboardUserDTO::getLearningTime)
                                .thenComparing((o1, o2) -> {
                                    if (o1.getFullName().isEmpty()) {
                                        return 1;
                                    } else if (o2.getFullName().isEmpty()) {
                                        return -1;
                                    } else {
                                        return o1.getFullName().compareToIgnoreCase(o2.getFullName());
                                    }
                                })
                        )

                        .toList().subList(0,Math.min(LEADERBOARD_RANK_LIMIT, listOfLeaderboardUser.size())))
                .year(year)
                .build();
    }

    @Override
    public Set<String> getExistedYears() {
        return leaderboardQueryManager.getExistedYears().stream()
                .filter(year -> year <= LocalDate.now().getYear())
                .map(Object::toString)
                .sorted(String::compareTo)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
