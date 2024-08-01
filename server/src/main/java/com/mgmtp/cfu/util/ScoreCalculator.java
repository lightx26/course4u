package com.mgmtp.cfu.util;

import com.mgmtp.cfu.enums.CourseLevel;
import com.mgmtp.cfu.enums.DurationUnit;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class ScoreCalculator {
    private final static int BASE_POINT = 500;
    private final static Map<CourseLevel, Double> levelWeights = Map.of(
            CourseLevel.BEGINNER, 1.0,
            CourseLevel.INTERMEDIATE, 1.5,
            CourseLevel.ADVANCED, 2.0
    );

    public static int calculateScore(CourseLevel level, ZonedDateTime startTime, ZonedDateTime endTime, int estimatedTime, DurationUnit durationUnit) {
        Duration diffTime = Duration.between(startTime, endTime);
        int actualHours = (int) diffTime.toHours();

        String unit = durationUnit.name() + "S";
        int estimatedHours = (int) (estimatedTime * (ChronoUnit.valueOf(unit).getDuration().toHours()));

        return calculateStandardPoint(level) + calculateBonusPoint(actualHours, estimatedHours);
    }

    private static int calculateStandardPoint(CourseLevel level) {
        return (int) (BASE_POINT * levelWeights.get(level));
    }

    private static int calculateBonusPoint(int actualHours, int estimatedHours) {
        return (int) (BASE_POINT * (1 - (double) actualHours / estimatedHours));
    }
}
