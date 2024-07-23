package com.mgmtp.cfu.dto.userdto;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MyScoreOfStatisticsDTO {
    private List<String> label;
    private Data data;
    @Setter
    @Getter
    @AllArgsConstructor
    @Builder
    public static class Data{
        List<Long> days;
        List<Long> scores;
    }


}
