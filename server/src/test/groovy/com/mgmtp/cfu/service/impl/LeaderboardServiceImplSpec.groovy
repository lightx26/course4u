package com.mgmtp.cfu.service.impl

import com.mgmtp.cfu.dto.leaderboarddto.LeaderboardUserDTO
import com.mgmtp.cfu.entity.User
import com.mgmtp.cfu.repository.queries.ScoreQueryManager
import com.mgmtp.cfu.service.LeaderboardService
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification
import spock.lang.Subject

class LeaderboardServiceImplSpec extends Specification {
    ScoreQueryManager leaderboardQueryManager = Mock()
    @Subject
    LeaderboardService leaderboardService = new LeaderboardServiceImpl(leaderboardQueryManager)

    def 'getLeaderboard'() {
        given:

        def authentication = Mock(Authentication) {
            getCredentials() >> User.builder().id(userId).build()
        }
        SecurityContextHolder.context.authentication = authentication
        leaderboardQueryManager.getLeaderboardUsers(_, _) >> List.of(
                LeaderboardUserDTO.builder().userId(1).fullName("").score(1).learningTime(1).build(),
                LeaderboardUserDTO.builder().userId(2).fullName("").score(1).learningTime(1).build(),
                LeaderboardUserDTO.builder().userId(3).fullName("").score(1).learningTime(1).build(),
                LeaderboardUserDTO.builder().userId(userId).fullName("").score(1).learningTime(1).build()
        )
        when:
        def result = leaderboardService.getLeaderboard(year)
        then:
        noExceptionThrown()
        where:
        userId | year
        4      | 2024
    }
    def 'getLeaderboardWiki'() {
        given:
        leaderboardQueryManager.getLeaderboardUsers(_, _) >> List.of(
                LeaderboardUserDTO.builder().userId(1).fullName("").score(1).learningTime(1).build(),
                LeaderboardUserDTO.builder().userId(2).fullName("").score(1).learningTime(1).build(),
                LeaderboardUserDTO.builder().userId(3).fullName("").score(1).learningTime(1).build(),
                LeaderboardUserDTO.builder().userId(userId).fullName("").score(1).learningTime(1).build()
        )
        when:
        def result = leaderboardService.getLeaderboardWiki(year)
        then:
        noExceptionThrown()
        where:
        userId | year
        4      | 2024
    }
    def 'getExistedYears'() {
        given:
        leaderboardQueryManager.getExistedYears() >> Set.of(2021)
        when:
        def result = leaderboardService.getExistedYears()
        then:
        result.size() == 1
    }

}
