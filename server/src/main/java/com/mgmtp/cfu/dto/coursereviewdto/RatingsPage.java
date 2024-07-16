package com.mgmtp.cfu.dto.coursereviewdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingsPage {
    private double averageRating = 0;
    private Map<Integer, Long> detailRatings = new HashMap<>(Map.ofEntries(
            Map.entry(1, 0L),
            Map.entry(2, 0L),
            Map.entry(3, 0L),
            Map.entry(4, 0L),
            Map.entry(5, 0L)))
    ;
}
