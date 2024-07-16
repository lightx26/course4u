package com.mgmtp.cfu.controller

import com.mgmtp.cfu.dto.BaseResponse
import com.mgmtp.cfu.dto.leaderboarddto.LeaderboardDTO
import com.mgmtp.cfu.dto.leaderboarddto.LeaderboardUserDTO
import com.mgmtp.cfu.service.LeaderboardService
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

class LeaderboardControllerSpec extends Specification {
    def leaderboardService = Mock(LeaderboardService)
    @Subject
    LeaderboardController leaderboardController = new LeaderboardController(leaderboardService)

    def 'getLeaderboard'() {
        given:
        // Mock setup
        leaderboardService.getLeaderboard(_ as int) >> { y ->
            return LeaderboardDTO.builder().year(y).leaderboardUserDTOs(new ArrayList<LeaderboardUserDTO>()).build()
        }

        when:
        ResponseEntity<LeaderboardDTO> response = leaderboardController.getLeaderboard(year)

        then:
        response.statusCode.value() == 200
        where:
        year                     | result
        LocalDate.now().year     | LocalDate.now().year
        LocalDate.now().year + 1 | LocalDate.now().year
        LocalDate.now().year - 2 | LocalDate.now().year - 2
    }

    def 'getYears'() {
        given:
        leaderboardService.getExistedYears()>> Set.of("2021")

        when:
        ResponseEntity<BaseResponse> response = leaderboardController.getExistedYear()
        then:
        response.statusCode.value() == 200
    }

}
