package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.leaderboarddto.LeaderboardUserDTO;
import com.mgmtp.cfu.service.LeaderboardService;
import com.mgmtp.cfu.service.WikiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WikiServiceImpl implements WikiService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private LeaderboardService leaderboardService;
    @Value("${wiki.service.scheduled.enabled}")
    private boolean isScheduledEnabled;
    @Value("${wiki.service.scheduled.url}")
    private String API_URL;
    @Value("${wiki.service.scheduled.token}")
    private String BEARER_TOKEN;
    @Value("${wiki.service.scheduled.space.key}")
    private String SPACE_KEY;
    @Override
    @Scheduled(cron = "${wiki.service.scheduled.period}")
    public void callWikiApi() {
        if(!isScheduledEnabled) {
            return;
        }
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
        String formattedDate = now.format(formatter);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + BEARER_TOKEN);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", String.format("Course4U Leaderboard of %s", formattedDate));
        requestBody.put("type", "page");

        Map<String, Object> space = new HashMap<>();
        space.put("key", SPACE_KEY);
        space.put("type", "global");
        requestBody.put("space", space);

        Map<String, Object> ancestors = new HashMap<>();
        ancestors.put("id", "376886648");
        requestBody.put("ancestors", new Map[] { ancestors });

        var leaderboard = leaderboardService.getLeaderboardWiki(now.getYear());
        Map<String, Object> wikiBody = new HashMap<>();
        wikiBody.put("value", generateListUserTable(
                new String[]{"TOP", "Full Name", "Email", "Learning Time (days)", "Score"},
                leaderboard.getLeaderboardUserDTOs())
        );
        wikiBody.put("representation", "wiki");

        Map<String, Object> body = new HashMap<>();
        body.put("wiki", wikiBody);
        requestBody.put("body", body);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
    }

    public static String generateListUserTable(String[] header, List<LeaderboardUserDTO> leaderboardUserDTOs) {
        if (header == null || header.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i < header.length;i++) {
            sb.append("||").append(header[i]).append("||");
        }
        sb.append("\n");
        for(int i = 0;i < leaderboardUserDTOs.size();i++) {
            sb.append("|")
                    .append(i + 1)
                    .append("|")
                    .append(leaderboardUserDTOs.get(i).getFullName().isEmpty() ? "Anonymous" : leaderboardUserDTOs.get(i).getFullName())
                    .append("|")
                    .append(leaderboardUserDTOs.get(i).getEmail())
                    .append("|")
                    .append(leaderboardUserDTOs.get(i).getLearningTime())
                    .append("|")
                    .append(leaderboardUserDTOs.get(i).getScore())
                    .append("|")
                    .append("\n");
        }
        return sb.toString();
    }
}
