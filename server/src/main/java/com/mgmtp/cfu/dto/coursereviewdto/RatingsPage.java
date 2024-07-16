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
    private Map<Integer, Long> detailRatings = new HashMap<>();
}
