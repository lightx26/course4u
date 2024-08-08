package com.mgmtp.cfu.service.impl

import com.mgmtp.cfu.dto.leaderboarddto.LeaderboardDTO
import com.mgmtp.cfu.dto.leaderboarddto.LeaderboardUserDTO
import com.mgmtp.cfu.service.LeaderboardService
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import spock.lang.Subject


import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class WikiServiceImplSpec extends Specification {

    RestTemplate restTemplate = Mock()
    LeaderboardService leaderboardService = Mock()

    @Subject
    WikiServiceImpl wikiService

    def setup() {
        wikiService = new WikiServiceImpl()
        wikiService.restTemplate = restTemplate
        wikiService.leaderboardService = leaderboardService
        wikiService.isScheduledEnabled = true
        wikiService.API_URL = 'https://example.com/api'
        wikiService.BEARER_TOKEN = 'token'
        wikiService.SPACE_KEY = 'spaceKey'
    }
    def "should call Wiki API with correct data"() {
        given:
        def now = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"))
        def formattedDate = now.format(DateTimeFormatter.ofPattern('yyyy/MM/dd HH:mm'))

        def leaderboardUserDTOs = [new LeaderboardUserDTO(username: 'user1', fullName: 'User One', email: 'user1@example.com', learningTime: 5, score: 100)]
        def leaderboardDTO = new LeaderboardDTO(leaderboardUserDTOs: leaderboardUserDTOs)

        leaderboardService.getLeaderboardWiki(now.getYear()) >> leaderboardDTO

        def expectedWikiTable = wikiService.generateListUserTable(
                ["TOP", "Username", "Full Name", "Email", "Learning Time (days)", "Score"] as String[],
                leaderboardUserDTOs
        )

        def requestBody = [
                title    : "Leaderboard ${formattedDate}",
                type     : "page",
                space    : [key: 'spaceKey', type: 'global'],
                ancestors: [[id: '376886648']],
                body     : [
                        wiki: [
                                value        : expectedWikiTable,
                                representation: "wiki"
                        ]
                ]
        ]

        def headers = new HttpHeaders()
        headers.set("Content-Type", "application/json")
        headers.set("Authorization", "Bearer token")

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers)

        when:
        wikiService.callWikiApi()

        then:
        restTemplate.exchange('https://example.com/api', HttpMethod.POST, entity, String.class) >> ResponseEntity.ok("Success")
    }

    def "should not call Wiki API when scheduling is disabled"() {
        given:
        wikiService.isScheduledEnabled = false

        when:
        wikiService.callWikiApi()

        then:
        0 * restTemplate.exchange(_, _, _, _)
    }
}
