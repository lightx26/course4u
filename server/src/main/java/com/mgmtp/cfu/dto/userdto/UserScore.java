package com.mgmtp.cfu.dto.userdto;

import lombok.*;

import java.util.List;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserScore {
    private int rank;
    private long score;
    private String year;
    private List<String> months;
    private List<Integer> scores;
    private List<Integer> learningTime;
}
