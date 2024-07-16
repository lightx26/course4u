package com.mgmtp.cfu.controller;

import com.mgmtp.cfu.dto.BaseResponse;
import com.mgmtp.cfu.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static com.mgmtp.cfu.util.Constant.LEADERBOARD_YEAR_LIMIT;

@RequestMapping("api/leaderboards")
@RestController
@RequiredArgsConstructor
public class LeaderboardController {
    private final LeaderboardService leaderboardService;

    @GetMapping()
    ResponseEntity<?> getLeaderboard(@RequestParam("year") int year){
        int currentYear= LocalDate.now().getYear();
        if(year<LEADERBOARD_YEAR_LIMIT||year>currentYear)
            year=currentYear;
        return ResponseEntity.ok().body(leaderboardService.getLeaderboard(year));
    }
    @GetMapping("/years")
    ResponseEntity<?> getExistedYear(){
        return ResponseEntity.ok().body(BaseResponse.builder().message("This includes all the years that have registrations in the system.").data(leaderboardService.getExistedYears()).build());

    }

}
