package com.mgmtp.cfu.service.impl

import com.mgmtp.cfu.dto.leaderboarddto.LeaderboardUserDTO
import com.mgmtp.cfu.entity.User
import com.mgmtp.cfu.repository.queries.LeaderboardQueryManager
import com.mgmtp.cfu.service.LeaderboardService
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification
import spock.lang.Subject

class LeaderboardServiceImplSpec extends Specification {
    LeaderboardQueryManager leaderboardQueryManager = Mock()
    @Subject
    LeaderboardService leaderboardService = new LeaderboardServiceImpl(leaderboardQueryManager)

    def 'getLeaderboard'() {
        given:

        def authentication = Mock(Authentication) {
            getCredentials() >> User.builder().id(userId).build()
        }
        SecurityContextHolder.context.authentication = authentication
        leaderboardQueryManager.getLeaderboardUsers(_, _) >> List.of(
                LeaderboardUserDTO.builder().userId(1).build(),
                LeaderboardUserDTO.builder().userId(2).build(),
                LeaderboardUserDTO.builder().userId(3).build(),
                LeaderboardUserDTO.builder().userId(userId).build()
        )
        when:
        def result = leaderboardService.getLeaderboard(year)
        then:

        result.leaderboardUserDTOs.size() == 4
        result.leaderboardUserDTOs.stream().anyMatch(a -> a.isMine())
        where:
        userId | year
        4      | 2024
        5      | 2023
    }
    def 'getLeaderboard'() {
        given:
        leaderboardQueryManager.getExistedYears()>>Set.of("2021")
        when:
        def result=leaderboardService.getExistedYears()
        then:
        result.size()==1
    }

}
