package com.mgmtp.cfu.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static com.mgmtp.cfu.util.Constant.DATE_TIME_FORMAT;

public class TimeConverter {

    // Constants for conversion
    private static final double SECONDS_IN_A_DAY = 86400; // 24 * 60 * 60
    private static final double MINUTES_IN_A_DAY = 1440; // 24 * 60
    private static final double HOURS_IN_A_DAY = 24;
    private static final double WEEKS_IN_A_DAY = 1.0 / 7;
    private static final double MONTHS_IN_A_DAY = 1.0 / 30.44; // Average days in a month

    public static LocalDateTime plus(LocalDateTime dateTime,String time) {
        if (time == null || time.isEmpty()) {
            throw new IllegalArgumentException("Time string cannot be null or empty.");
        }

        time = time.toLowerCase();
        double value;
        String unit;

        // Extract the numeric value and unit from the string
        try {
            value = Double.parseDouble(time.replaceAll("[^0-9.]", ""));
            unit = time.replaceAll("[0-9.]", "");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid time format.");
        }

        return switch (unit) {
            case "d" -> dateTime.plus((long) value, ChronoUnit.DAYS);
            case "h" -> dateTime.plus((long) value, ChronoUnit.HOURS);
            case "min" -> dateTime.plus((long) value, ChronoUnit.MINUTES);
            case "s" -> dateTime.plus((long) value, ChronoUnit.SECONDS);
            case "wk" -> dateTime.plus((long) (value * 7), ChronoUnit.DAYS);
            case "mo" -> dateTime.plus((long) (value * MONTHS_IN_A_DAY), ChronoUnit.DAYS);
            default -> throw new IllegalArgumentException("Unknown time unit.");
        };
    }

    public static String convertDayTimeToString(LocalDateTime dateTime){
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return dateTime.format(formatter);
    }
}

